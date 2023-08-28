package com.demo.baseproject.ads.unity

import android.app.Activity
import com.demo.baseproject.ads.AdServer
import com.demo.baseproject.ads.AdType
import com.demo.baseproject.ads.listeners.AdStatusListener
import com.demo.baseproject.ads.listeners.FullScreenAdListener
import com.demo.baseproject.events.EventLogger
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import com.unity3d.ads.UnityAdsShowOptions


class UnityInterstitialAdServer(
    private val ctx: Activity,
    private val adUnitId: String,
    private val adStatusListener: AdStatusListener
) : FullScreenAdListener {

    private var isAdLoaded = false

    init {
        UnityAds.load(adUnitId, object : IUnityAdsLoadListener {
            override fun onUnityAdsAdLoaded(placementId: String?) {
                adStatusListener.onAdLoaded()
                EventLogger.logAdLoaded(AdType.INTERSTITIAL, AdServer.UNITY)
                isAdLoaded = true
            }

            override fun onUnityAdsFailedToLoad(
                placementId: String?,
                error: UnityAds.UnityAdsLoadError?,
                message: String?
            ) {
                adStatusListener.onAdFailed(error?.name)
                EventLogger.logAdFailed(AdType.INTERSTITIAL, AdServer.UNITY, error?.name)
            }
        })
    }

    override fun show() {
        if (isAdLoaded) {
            UnityAds.show(
                ctx,
                adUnitId,
                UnityAdsShowOptions(),
                object : IUnityAdsShowListener {
                    override fun onUnityAdsShowFailure(
                        placementId: String?,
                        error: UnityAds.UnityAdsShowError?,
                        message: String?
                    ) {
                        adStatusListener.onAdFailed(error?.name)
                        EventLogger.logAdFailed(AdType.INTERSTITIAL, AdServer.UNITY, error?.name)
                    }

                    override fun onUnityAdsShowStart(placementId: String?) {
                    }

                    override fun onUnityAdsShowClick(placementId: String?) {
                    }

                    override fun onUnityAdsShowComplete(
                        placementId: String?,
                        state: UnityAds.UnityAdsShowCompletionState?
                    ) {
                        adStatusListener.onAdImpression()
                        EventLogger.logAdImpression(AdType.INTERSTITIAL, AdServer.UNITY)
                    }
                }
            )
        }
    }
}