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
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

/**
 * A class that serves a Admob Rewarded Video Ads. All the methods in this class are self-explanatory, hence not documented.
 */
class AdmobInterstitialAdServer(
    private val activity: Activity,
    private val adUnitId: String,
    private val eventLogger: EventLogger,
    private val adStatusListener: AdStatusListener?
) : FullScreenAdListener {

    private var interstitialAd: InterstitialAd? = null

    init {
        initAdAndSetListener()
    }

    private fun initAdAndSetListener() {
        InterstitialAd.load(
            activity,
            adUnitId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    adStatusListener?.onAdFailed(adError.message)
                    eventLogger.logAdFailed(AdType.INTERSTITIAL, AdServer.ADMOB, adError.message)
                }

                override fun onAdLoaded(rewardedAd: InterstitialAd) {
                    super.onAdLoaded(rewardedAd)
                    interstitialAd = rewardedAd
                    setScreenListener()
                    adStatusListener?.onAdLoaded()
                    eventLogger.logAdLoaded(AdType.INTERSTITIAL, AdServer.ADMOB)
                }
            })
    }

    private fun setScreenListener() {
        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdImpression() {
                super.onAdImpression()
                adStatusListener?.onAdImpression()
                eventLogger.logAdImpression(AdType.INTERSTITIAL, AdServer.ADMOB)
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                adStatusListener?.onAdFailed(p0.message)
                eventLogger.logAdFailed(AdType.INTERSTITIAL, AdServer.ADMOB, p0.message)
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                adStatusListener?.onDismissFullScreenAd()
            }
        }
    }

    override fun show() {
        interstitialAd?.show(activity)
    }
}