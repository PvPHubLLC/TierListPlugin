package co.pvphub.tierlist

import co.pvphub.tierlist.api.data.Tierlists
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

class TierListPlaceholders(val plugin: TierListPlugin) : PlaceholderExpansion() {
    override fun getIdentifier(): String {
        return "tierlist"
    }

    override fun getAuthor(): String = plugin.pluginMeta.authors.getOrNull(0) ?: "unknown"

    override fun getVersion(): String = plugin.pluginMeta.version

    /**
     * Players tier:
     * %tierlist_tier_:list%
     * %tierlist_peak_:list%
     * %tierlist_pos_:list_:tier_:position%
     */
    override fun onPlaceholderRequest(player: Player?, params: String): String? {
        val split = params.split("_")

        if (split.size > 4 && split[0] == "pos") {
            val tierListId = Tierlists.find(split[1]) ?: return "Unknown TierList ${split[1]}"
            val tier = split[2].toIntOrNull() ?: return "Invalid Integer ${split[2]}"
            val position = split[3].toIntOrNull() ?: return "Invalid Integer ${split[3]}"

            val tierList = plugin.tierLists[tierListId] ?: return "No tierlist data cached, check the config.yml."

            val user = tierList.getUserAt(tier, position) ?: return "-"
            val returnData = when (split[4]) {
                "name", "username" -> user.username
                "tier" -> user.tier.toString()
                "uuid" -> user.uuid.toString()
                "points" -> user.points.toString()
                "pos" -> user.positionInTier.toString()
                "region" -> user.region
                else -> null
            }

            return returnData
        }

        if (player == null) return null
        val data = plugin.userData[player.uniqueId] ?: return plugin.config.getString("unranked")

        if (split.size == 2 && split[0] == "tier") {
            val tierList = Tierlists.find(split[1]) ?: return plugin.config.getString("unranked")
            val ranking = data.getRanking(tierList) ?: return plugin.config.getString("unranked")

            return plugin.config.getString("tier")!!
                .replace("{{color}}", plugin.config.getString("color.tier.${ranking.tier}")!!)
                .replace("{{prefix}}", "")
                .replace("{{tier}}", ranking.tier.toString())
        } else if (split.size == 2 && split[0] == "peak") {
            val tierList = Tierlists.find(split[1]) ?: return plugin.config.getString("unranked")
            val ranking = data.getRanking(tierList) ?: return plugin.config.getString("unranked")

            return plugin.config.getString("tier")!!
                .replace("{{color}}", plugin.config.getString("color.tier.${ranking.peakTier}")!!)
                .replace("{{prefix}}", "")
                .replace("{{tier}}", ranking.peakTier.toString())
        }

        return null
    }
}