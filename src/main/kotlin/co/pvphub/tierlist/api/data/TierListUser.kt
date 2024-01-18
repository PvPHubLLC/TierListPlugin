package co.pvphub.tierlist.api.data

import co.pvphub.tierlist.api.toUUIDNoDashes
import com.google.gson.annotations.SerializedName

data class TierListUser(
    private val uuidString: String,
    val name: String,
    val rankings: HashMap<Tierlists, TierListRanking>,
    val region: String,
    val points: Int,
    val overall: Int
    // todo badges
) {

    @SerializedName("_uuid")
    val uuid = uuidString.toUUIDNoDashes()

    fun getRanking(tierList: Tierlists) = rankings[tierList]
}