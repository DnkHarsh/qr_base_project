package com.demo.baseproject.ads.listeners

/**
 * Defines listener for Ad status after firing ad load request.
 */
interface AdStatusListener {

    fun onAdLoaded() {}

    fun onAdImpression() {}

    fun onAdFailed(error: String?) {}

    fun onUserEarnedReward() {}
}