package co.pvphub.tierlist.api.data

data class TierListData(
    val rankings: Array<Array<Array<Any>>>,
    val players: Map<String, BasicTierListUserInfo>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TierListData

        if (!rankings.contentDeepEquals(other.rankings)) return false
        if (players != other.players) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rankings.contentDeepHashCode()
        result = 31 * result + players.hashCode()
        return result
    }
}