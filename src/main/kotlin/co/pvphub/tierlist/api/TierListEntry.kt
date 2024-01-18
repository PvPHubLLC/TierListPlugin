package co.pvphub.tierlist.api

import java.util.UUID

data class TierListEntry(
    val uuid: UUID,
    val username: String,
    val region: String,
    val points: Int,
    val tier: Int,
    val positionInTier: Int
)