package com.demo.baseproject.ads.admob

import android.app.Activity
import com.demo.baseproject.ads.AdServer
import com.demo.baseproject.ads.AdType
import com.demo.baseproject.ads.listeners.AdStatusListener
import com.demo.baseproject.ads.listeners.FullScreenAdListener
import com.demo.baseproject.events.EventLogger
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

/**
 * A class that serves a Admob Rewarded Video Ads. All the methods in this class are self-explanatory, hence not documented.
 */
class AdmobRewardedVideoAdServer(
    private val ctx: Activity,
    private val adUnitId: String,
    private val adStatusListener: AdStatusListener
) : FullScreenAdListener {
    private var rewardedAdObject: RewardedAd? = null

    init {
        initAdAndSetListener()
    }

    private fun initAdAndSetListener() {
        RewardedAd.load(
            ctx,
            adUnitId,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adStatusListener.onAdFailed(adError.message)
                    EventLogger.logAdFailed(AdType.REWARDED, AdServer.ADMOB, adError.message)
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    adStatusListener.onAdLoaded()
                    EventLogger.logAdLoaded(AdType.REWARDED, AdServer.ADMOB)
                    rewardedAdObject = rewardedAd
                    setScreenListener()
                }
            })
    }

    private fun setScreenListener() {
        rewardedAdObject?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                adStatusListener.onAdFailed(p0.message)
                EventLogger.logAdFailed(AdType.REWARDED, AdServer.ADMOB, p0.message)
            }
        }
    }

    override fun show() {
        rewardedAdObject?.show(ctx) {
            adStatusListener.onUserEarnedReward()
            EventLogger.logAdRewardEarned(AdType.REWARDED, AdServer.ADMOB)
        }
    }
}