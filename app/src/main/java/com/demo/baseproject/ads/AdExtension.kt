package com.demo.baseproject.ads

import com.demo.baseproject.application.BaseApplication

fun getAdResponse(screenName: String, adType: AdType): AdPlacementResponse? {
    BaseApplication.adResponse?.adList?.find { it.screenName == screenName }?.adPlacements?.let { adPlacements ->
        return adPlacements.find { it.adType == adType }
    }
    return null
}