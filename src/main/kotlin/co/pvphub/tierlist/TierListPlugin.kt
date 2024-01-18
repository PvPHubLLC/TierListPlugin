package co.pvphub.tierlist

import co.pvphub.tierlist.api.TierList
import co.pvphub.tierlist.api.TierListAPI
import co.pvphub.tierlist.api.data.TierListUser
import co.pvphub.tierlist.api.data.Tierlists
import com.mattmx.ktgui.commands.simpleCommand
import com.mattmx.ktgui.dsl.event
import com.mattmx.ktgui.utils.not
import org.bukkit.Bukkit
import org.bukkit.event.EventPriority
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class TierListPlugin : JavaPlugin() {
    private val api = TierListAPI()
    val userData = Collections.synchronizedMap(hashMapOf<UUID, TierListUser>())
    val tierLists = Collections.synchronizedMap(hashMapOf<Tierlists, TierList>())

    override fun onEnable() {
        instance = this
        saveDefaultConfig()

        // Check config caching
        for (tierListId in config.getConfigurationSection("refresh")!!.getKeys(false)) {
            val list = Tierlists.find(tierListId) ?: continue
            val period = config.getLong("refresh.$tierListId")

            Bukkit.getScheduler().runTaskTimerAsynchronously(this, { task ->
                logger.info("Refreshing cache for $tierListId")
                val data = api.getTierListData(list)
                    ?: return@runTaskTimerAsynchronously logger.warning("Refresh of cache for $tierListId failed")
                tierLists[list] = TierList(data)
                logger.info("Refresh of cache for $tierListId completed")
            }, 0, period)
        }

        event<AsyncPlayerPreLoginEvent>(priority = EventPriority.MONITOR) {
            if (loginResult != AsyncPlayerPreLoginEvent.Result.ALLOWED) return@event

            Bukkit.getScheduler().runTaskAsynchronously(this@TierListPlugin) { ->
                val data = api.searchForUser(name)
                    ?: return@runTaskAsynchronously
                userData[uniqueId] = data
            }
        }

        event<PlayerQuitEvent> {
            userData.remove(player.uniqueId)
        }

        simpleCommand {
            name = "tierlist"
            permission = "tierlist.command"

            executes {
                source.sendMessage(!"&dTierList v${pluginMeta.version} by ${pluginMeta.authors.joinToString(", ")}")
            }

            subCommands += simpleCommand {
                name = "reload"
                permission = "tierlist.command.reload"

                executes {
                    reloadConfig()
                    source.sendMessage(!"&dReloaded config.yml")
                }
            }
        }.register(false)

        TierListPlaceholders(this).register()
    }

    companion object {
        private lateinit var instance: TierListPlugin
        fun get() = instance
    }

}