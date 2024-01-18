package co.pvphub.tierlist.api.data

import co.pvphub.tierlist.api.TierListAPI
import com.google.gson.annotations.SerializedName

data class BasicTierListUserInfo(
    @SerializedName("name")
    val username: String,
    val region: String,
    val points: Double
) {
    fun getUser(api: TierListAPI) = api.searchForUser(username)
}