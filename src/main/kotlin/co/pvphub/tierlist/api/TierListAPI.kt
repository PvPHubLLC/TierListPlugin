package co.pvphub.tierlist.api

import co.pvphub.tierlist.TierListPlugin
import co.pvphub.tierlist.api.data.TierListData
import co.pvphub.tierlist.api.data.TierListRanking
import co.pvphub.tierlist.api.data.TierListUser
import co.pvphub.tierlist.api.data.Tierlists
import com.github.kittinunf.fuel.Fuel
import org.bukkit.Bukkit
import java.util.UUID
import java.util.concurrent.CompletableFuture

class TierListAPI() {
    private val root = "https://mctiers.com/api"

    private fun <T> returnAsync(method: () -> T?) : CompletableFuture<T> {
        val future = CompletableFuture<T>()

        Bukkit.getScheduler().runTaskAsynchronously(TierListPlugin.get()) {->
            val value = method()
            if (value == null) {
                future.completeExceptionally(Error("Test"))
            } else future.complete(value)
        }

        return future
    }

    fun searchForUserAsync(username: String) = returnAsync { searchForUser(username) }
    fun searchForUser(username: String): TierListUser? {
         return Fuel.get("$root/search_profile/$username")
            .response()
            .second
            .jsonBody<TierListUser?>(TierListUser::class.java)
    }

    fun getUserAsync(uuid: UUID) = returnAsync { getUser(uuid) }
    fun getUser(uuid: UUID) : HashMap<Tierlists, TierListRanking>? {
        return Fuel.get("$root/rankings/${uuid.toString().replace("-", "")}")
            .response()
            .second
            .jsonBody<HashMap<Tierlists, TierListRanking>?>(HashMap::class.java)
    }

    fun getTierListAsync(tierlist: Tierlists, from: Int = 0, count: Int = 65535) = returnAsync { getTierListData(tierlist, from, count) }
    fun getTierListData(tierlist: Tierlists, from: Int = 0, count: Int = 65535): TierListData? {
        return Fuel.get("$root/tier/$tierlist?from=$from&count=$count")
            .response()
            .second
            .jsonBody<TierListData?>(TierListData::class.java)
    }

}