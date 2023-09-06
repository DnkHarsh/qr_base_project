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
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback

/**
 * A class that serves a Gam Rewarded Videos Ad. All the methods in this class are self-explanatory, hence not documented.
 */
class GamInterstitialAdServer(
    private val activity: Activity,
    private val adUnitId: String,
    private val adStatusListener: AdStatusListener?
) : FullScreenAdListener {
    private var interstitialAd: AdManagerInterstitialAd? = null

    init {
        initAdAndSetListener()
    }

    private fun initAdAndSetListener() {
        AdManagerInterstitialAd.load(
            activity,
            adUnitId,
            AdManagerAdRequest.Builder().build(),
            object : AdManagerInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: AdManagerInterstitialAd) {
                    super.onAdLoaded(ad)
                    adStatusListener?.onAdLoaded()
                    EventLogger.logAdLoaded(AdType.INTERSTITIAL, AdServer.GAM)
                    interstitialAd = ad
                    setScreenListener()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    adStatusListener?.onAdFailed(loadAdError.message)
                    EventLogger.logAdFailed(AdType.INTERSTITIAL, AdServer.GAM, loadAdError.message)
                }
            }
        )
    }

    private fun setScreenListener() {
        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdImpression() {
                super.onAdImpression()
                adStatusListener?.onAdImpression()
                EventLogger.logAdImpression(AdType.INTERSTITIAL, AdServer.GAM)
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                adStatusListener?.onAdFailed(p0.message)
                EventLogger.logAdFailed(AdType.INTERSTITIAL, AdServer.GAM, p0.message)
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