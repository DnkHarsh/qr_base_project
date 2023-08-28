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
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback

/**
 * A class that serves a Gam Rewarded Videos Ad. All the methods in this class are self-explanatory, hence not documented.
 */
class GamRewardedInterstitialAdServer(
    private val ctx: Activity,
    private val adUnitId: String,
    private val adStatusListener: AdStatusListener
) : FullScreenAdListener {
    private var rewardedAdObject: RewardedInterstitialAd? = null

    init {
        initAdAndSetListener()
    }

    private fun initAdAndSetListener() {
        RewardedInterstitialAd.load(
            ctx,
            adUnitId,
            AdManagerAdRequest.Builder().build(),
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adStatusListener.onAdFailed(adError.message)
                    EventLogger.logAdFailed(
                        AdType.REWARDED_INTERSTITIAL,
                        AdServer.GAM,
                        adError.message
                    )
                }

                override fun onAdLoaded(rewardedAd: RewardedInterstitialAd) {
                    adStatusListener.onAdLoaded()
                    EventLogger.logAdLoaded(AdType.REWARDED_INTERSTITIAL, AdServer.GAM)
                    rewardedAdObject = rewardedAd
                    setScreenListener()
                }
            })
    }

    private fun setScreenListener() {
        rewardedAdObject?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                adStatusListener.onAdFailed(p0.message)
                EventLogger.logAdFailed(AdType.REWARDED_INTERSTITIAL, AdServer.GAM, p0.message)
            }
        }
    }

    override fun show() {
        rewardedAdObject?.show(ctx) {
            adStatusListener.onUserEarnedReward()
            EventLogger.logAdRewardEarned(AdType.REWARDED_INTERSTITIAL, AdServer.GAM)
        }
    }
}