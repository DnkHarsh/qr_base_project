package com.demo.baseproject.ads

fun getAdResponse(
    adResponse: AdResponse?,
    screenName: String,
    adType: AdType
): AdPlacementResponse? {
    adResponse?.adList?.find { it.screenName == screenName }?.adPlacements?.let { adPlacements ->
        return adPlacements.find { it.adType == adType }
    }
    return null
}