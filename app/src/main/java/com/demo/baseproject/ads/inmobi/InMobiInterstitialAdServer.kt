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


class InMobiInterstitialAdServer(
    private val ctx: Activity,
    private val adUnitId: String,
    private val adStatusListener: AdStatusListener
) : FullScreenAdListener {

    private var interstitialAd: InMobiInterstitial
    private var isAdLoaded = false

    init {
        interstitialAd = InMobiInterstitial(ctx, adUnitId.toLong(), object :
            InterstitialAdEventListener() {
            override fun onAdFetchFailed(p0: InMobiInterstitial, p1: InMobiAdRequestStatus) {
                super.onAdFetchFailed(p0, p1)
                adStatusListener.onAdFailed(p1.message)
                EventLogger.logAdFailed(AdType.INTERSTITIAL, AdServer.INMOBI, p1.message)
            }

            override fun onAdLoadSucceeded(p0: InMobiInterstitial, p1: AdMetaInfo) {
                super.onAdLoadSucceeded(p0, p1)
                adStatusListener.onAdLoaded()
                EventLogger.logAdLoaded(AdType.INTERSTITIAL, AdServer.INMOBI)
                isAdLoaded = true
            }

            override fun onAdLoadFailed(p0: InMobiInterstitial, p1: InMobiAdRequestStatus) {
                super.onAdLoadFailed(p0, p1)
                adStatusListener.onAdFailed(p1.message)
                EventLogger.logAdFailed(AdType.INTERSTITIAL, AdServer.INMOBI, p1.message)
            }

            override fun onAdImpression(p0: InMobiInterstitial) {
                super.onAdImpression(p0)
                adStatusListener.onAdImpression()
                EventLogger.logAdImpression(AdType.INTERSTITIAL, AdServer.INMOBI)
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