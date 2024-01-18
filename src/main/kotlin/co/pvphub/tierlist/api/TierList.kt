package co.pvphub.tierlist.api

import co.pvphub.tierlist.api.data.TierListData

class TierList(
    tierListData: TierListData
) {
    private val dataMapped = hashMapOf<Int, ArrayList<TierListEntry>>(
        1 to arrayListOf(),
        2 to arrayListOf(),
        3 to arrayListOf(),
        4 to arrayListOf(),
        5 to arrayListOf()
    )

    init {
        for (tier in dataMapped.keys) {
            val index = tier - 1
            val entries = tierListData.rankings.getOrNull(index) ?: continue
            dataMapped[tier]?.addAll(entries.map { rankingEntry ->
                val uuidStr = rankingEntry[0].toString()
                val position = rankingEntry[1].toString().toDouble().toInt()
                val userData = tierListData.players[uuidStr]!!
                val uuid = uuidStr.toUUIDNoDashes()
                TierListEntry(uuid, userData.username, userData.region, userData.points.toInt(), tier, position)
            })
        }
    }

    fun getListForTier(tier: Int) = dataMapped[tier]
    fun getUserAt(tier: Int, position: Int) = getListForTier(tier)?.getOrNull(position)

    operator fun plus(tierList: TierList) = add(tierList)
    operator fun plusAssign(tierList: TierList) {
        add(tierList)
    }

    fun add(tierList: TierList) = apply {
        for (tier in dataMapped.keys) {
            val data = tierList.dataMapped[tier]
                ?: continue
            dataMapped[tier]?.addAll(data)
        }
    }
}