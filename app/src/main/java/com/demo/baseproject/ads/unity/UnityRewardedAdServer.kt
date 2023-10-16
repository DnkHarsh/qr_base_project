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

class UnityRewardedAdServer(
    private val activity: Activity,
    private val adUnitId: String,
    private val eventLogger: EventLogger,
    private val adStatusListener: AdStatusListener
) : FullScreenAdListener {

    private var isAdLoaded = false

    init {
        UnityAds.load(adUnitId, object : IUnityAdsLoadListener {
            override fun onUnityAdsAdLoaded(placementId: String?) {
                isAdLoaded = true
                adStatusListener.onAdLoaded()
                eventLogger.logAdLoaded(AdType.REWARDED, AdServer.UNITY)
            }

            override fun onUnityAdsFailedToLoad(
                placementId: String?,
                error: UnityAds.UnityAdsLoadError?,
                message: String?
            ) {
                adStatusListener.onAdFailed(error?.name)
                eventLogger.logAdFailed(AdType.REWARDED, AdServer.UNITY, error?.name)
            }
        })
    }

    override fun show() {
        if (isAdLoaded) {
            UnityAds.show(
                activity,
                adUnitId,
                UnityAdsShowOptions(),
                object : IUnityAdsShowListener {
                    override fun onUnityAdsShowFailure(
                        placementId: String?,
                        error: UnityAds.UnityAdsShowError?,
                        message: String?
                    ) {
                        adStatusListener.onAdFailed(error?.name)
                        eventLogger.logAdFailed(AdType.REWARDED, AdServer.UNITY, error?.name)
                    }

                    override fun onUnityAdsShowStart(placementId: String?) {
                    }

                    override fun onUnityAdsShowClick(placementId: String?) {
                    }

                    override fun onUnityAdsShowComplete(
                        placementId: String?,
                        state: UnityAds.UnityAdsShowCompletionState?
                    ) {
                        if (state?.equals(UnityAds.UnityAdsShowCompletionState.COMPLETED) == true) {
                            adStatusListener.onUserEarnedReward()
                            eventLogger.logAdRewardEarned(AdType.REWARDED, AdServer.UNITY)
                        }
                        adStatusListener.onDismissFullScreenAd()
                    }
                }
            )
        }
    }
}