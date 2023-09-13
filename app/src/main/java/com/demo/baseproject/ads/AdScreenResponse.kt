package com.demo.baseproject.ads

import com.google.gson.annotations.SerializedName

data class AdScreenResponse(
    @SerializedName("screen_name")
    val screenName: String,
    @SerializedName("ad_placements")
    val adPlacements: List<AdPlacementResponse>
)