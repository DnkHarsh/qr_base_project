package com.demo.baseproject.ads

import com.google.gson.annotations.SerializedName

data class AdResponse(
    @SerializedName("ad_list")
    val adList: List<AdScreenResponse>
)