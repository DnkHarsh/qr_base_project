package com.demo.baseproject.ads.inmobi

import android.app.Activity
import com.demo.baseproject.ads.AdServer
import com.demo.baseproject.ads.AdType
import com.demo.baseproject.ads.listeners.AdStatusListener
import com.demo.baseproject.ads.listeners.FullScreenAdListener
import com.demo.baseproject.events.EventLogger
import com.inmobi.ads.AdMetaInfo
import com.inmobi.ads.InMobiAdRequestStatus
import com.inmobi.ads.InMobiInterstitial
import com.inmobi.ads.listeners.InterstitialAdEventListener


class InMobiRewardedAdServer(
    private val activity: Activity,
    private val adUnitId: String,
    private val eventLogger: EventLogger,
    private val adStatusListener: AdStatusListener
) : FullScreenAdListener {

    private var interstitialAd: InMobiInterstitial
    private var isAdLoaded = false

    init {
        interstitialAd = InMobiInterstitial(activity, adUnitId.toLong(), object :
            InterstitialAdEventListener() {
            override fun onAdLoadSucceeded(p0: InMobiInterstitial, p1: AdMetaInfo) {
                super.onAdLoadSucceeded(p0, p1)
                isAdLoaded = true
                adStatusListener.onAdLoaded()
                eventLogger.logAdLoaded(AdType.REWARDED, AdServer.INMOBI)
            }

            override fun onAdLoadFailed(p0: InMobiInterstitial, p1: InMobiAdRequestStatus) {
                super.onAdLoadFailed(p0, p1)
                adStatusListener.onAdFailed(p1.message)
                eventLogger.logAdFailed(AdType.REWARDED, AdServer.INMOBI, p1.message)
            }

            override fun onRewardsUnlocked(p0: InMobiInterstitial, p1: MutableMap<Any, Any>?) {
                super.onRewardsUnlocked(p0, p1)
                adStatusListener.onUserEarnedReward()
                eventLogger.logAdRewardEarned(AdType.REWARDED, AdServer.INMOBI)
            }

            override fun onAdDismissed(p0: InMobiInterstitial) {
                super.onAdDismissed(p0)
                adStatusListener.onDismissFullScreenAd()
            }
        })
        interstitialAd.load()
    }

    override fun show() {
        if (isAdLoaded) {
            interstitialAd.show()
        }
    }
}