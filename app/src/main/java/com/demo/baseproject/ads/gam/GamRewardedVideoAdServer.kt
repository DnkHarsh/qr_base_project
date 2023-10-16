package com.demo.baseproject.ads.gam

import android.app.Activity
import com.demo.baseproject.ads.AdServer
import com.demo.baseproject.ads.AdType
import com.demo.baseproject.ads.listeners.AdStatusListener
import com.demo.baseproject.ads.listeners.FullScreenAdListener
import com.demo.baseproject.events.EventLogger
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

/**
 * A class that serves a Gam Rewarded Videos Ad. All the methods in this class are self-explanatory, hence not documented.
 */
class GamRewardedVideoAdServer(
    private val activity: Activity,
    private val adUnitId: String,
    private val eventLogger: EventLogger,
    private val adStatusListener: AdStatusListener
) : FullScreenAdListener {

    private var rewardedAdObject: RewardedAd? = null

    init {
        initAdAndSetListener()
    }

    private fun initAdAndSetListener() {
        RewardedAd.load(
            activity,
            adUnitId,
            AdManagerAdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    adStatusListener.onAdFailed(adError.message)
                    eventLogger.logAdFailed(AdType.REWARDED, AdServer.GAM, adError.message)
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    super.onAdLoaded(rewardedAd)
                    rewardedAdObject = rewardedAd
                    setScreenListener()
                    adStatusListener.onAdLoaded()
                    eventLogger.logAdLoaded(AdType.REWARDED, AdServer.GAM)
                }
            })
    }

    private fun setScreenListener() {
        rewardedAdObject?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                adStatusListener.onAdFailed(p0.message)
                eventLogger.logAdFailed(AdType.REWARDED, AdServer.GAM, p0.message)
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                adStatusListener.onDismissFullScreenAd()
            }
        }
    }

    override fun show() {
        rewardedAdObject?.show(activity) {
            adStatusListener.onUserEarnedReward()
            eventLogger.logAdRewardEarned(AdType.REWARDED, AdServer.GAM)
        }
    }
}