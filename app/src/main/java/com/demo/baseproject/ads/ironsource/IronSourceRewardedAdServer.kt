package com.demo.baseproject.ads.ironsource

import com.demo.baseproject.ads.AdServer
import com.demo.baseproject.ads.AdType
import com.demo.baseproject.ads.listeners.AdStatusListener
import com.demo.baseproject.ads.listeners.FullScreenAdListener
import com.demo.baseproject.events.EventLogger
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.model.Placement
import com.ironsource.mediationsdk.sdk.LevelPlayRewardedVideoManualListener
import javax.inject.Inject

/**
 * A class that serves a Iron Source Rewarded Video Ads. All the methods in this class are self-explanatory, hence not documented.
 */
class IronSourceRewardedAdServer(
    private val adUnitId: String,
    private val eventLogger: EventLogger,
    private val adStatusListener: AdStatusListener
) : FullScreenAdListener {

    init {
        initAdAndSetListener()
    }

    private fun initAdAndSetListener() {
        IronSource.setLevelPlayRewardedVideoManualListener(object :
            LevelPlayRewardedVideoManualListener {
            override fun onAdOpened(adInfo: AdInfo) {
            }

            override fun onAdClosed(adInfo: AdInfo) {
                adStatusListener.onDismissFullScreenAd()
            }

            override fun onAdReady(p0: AdInfo?) {
                adStatusListener.onAdLoaded()
                eventLogger.logAdLoaded(AdType.REWARDED, AdServer.IRONSOURCE)
            }

            override fun onAdLoadFailed(p0: IronSourceError?) {
                adStatusListener.onAdFailed(p0?.errorMessage)
                eventLogger.logAdFailed(AdType.REWARDED, AdServer.IRONSOURCE, p0?.errorMessage)
            }

            override fun onAdRewarded(placement: Placement, adInfo: AdInfo) {
                adStatusListener.onUserEarnedReward()
                eventLogger.logAdRewardEarned(AdType.REWARDED, AdServer.IRONSOURCE)
            }

            override fun onAdShowFailed(error: IronSourceError, adInfo: AdInfo) {
                adStatusListener.onAdFailed(error.errorMessage)
                eventLogger.logAdFailed(AdType.REWARDED, AdServer.IRONSOURCE, error.errorMessage)
            }

            override fun onAdClicked(placement: Placement, adInfo: AdInfo) {
            }
        })
        IronSource.loadRewardedVideo()
    }

    override fun show() {
        if (IronSource.isRewardedVideoAvailable()) {
            IronSource.showRewardedVideo(adUnitId)
        }
    }
}