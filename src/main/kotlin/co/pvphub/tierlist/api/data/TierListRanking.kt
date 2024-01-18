package co.pvphub.tierlist.api.data

import com.google.gson.annotations.SerializedName

data class TierListRanking(
    val tier: Int,
    val pos: Int,
    @SerializedName("peak_tier") val peakTier: Int,
    @SerializedName("peak_pos") val peakPos: Int,
    val attained: Int,
    val retired: Boolean
)
