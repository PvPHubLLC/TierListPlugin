package co.pvphub.tierlist.api

import co.pvphub.tierlist.api.data.Tierlists
import java.util.*

fun main() {
    val api = TierListAPI()
    println(api.searchForUser("ItzRealMe"))

    val vanillaData = api.getTierListData(Tierlists.VANILLA)
        ?: return
    val vanilla = TierList(vanillaData)
    println(vanilla.getUserAt(1, 0))
    println(vanilla.getUserAt(5, 100))

    println(api.getUser(UUID.fromString("d219c8ee-d32e-4da2-b22e-0aa69d36c88a")))
}