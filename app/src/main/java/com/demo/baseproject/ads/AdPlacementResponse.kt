package com.demo.baseproject.ads

import com.google.gson.annotations.SerializedName

data class AdPlacementResponse(
    @SerializedName("ad_type")
    val adType: AdType,
    @SerializedName("ad_server")
    val adServer: AdServer,
    @SerializedName("ad_unit_id")
    val adUnitId: String,
    @SerializedName("template_type")
    val templateType: TemplateType?
)