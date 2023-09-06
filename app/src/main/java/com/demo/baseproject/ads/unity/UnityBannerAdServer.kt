package com.demo.baseproject.ads.unity

import android.app.Activity
import android.view.ViewGroup
import com.demo.baseproject.ads.AdServer
import com.demo.baseproject.ads.AdType
import com.demo.baseproject.ads.listeners.AdStatusListener
import com.demo.baseproject.ads.listeners.BannerAdListener
import com.demo.baseproject.events.EventLogger
import com.unity3d.services.banners.BannerErrorInfo
import com.unity3d.services.banners.BannerView
import com.unity3d.services.banners.UnityBannerSize

class UnityBannerAdServer(
    private val activity: Activity,
    private val adLayout: ViewGroup,
    private val adUnitId: String,
    private val adStatusListener: AdStatusListener?
) : BannerAdListener {

    private var adView = BannerView(activity, adUnitId, UnityBannerSize(320, 50))

    init {
        adView.listener = object : BannerView.IListener {
            override fun onBannerLoaded(bannerAdView: BannerView?) {
                adStatusListener?.onAdLoaded()
                EventLogger.logAdLoaded(AdType.BANNER, AdServer.UNITY)
                adStatusListener?.onAdImpression()
            }

            override fun onBannerClick(bannerAdView: BannerView?) {
            }

            override fun onBannerFailedToLoad(
                bannerAdView: BannerView?,
                errorInfo: BannerErrorInfo?
            ) {
                adStatusListener?.onAdFailed(errorInfo?.errorMessage)
                EventLogger.logAdFailed(AdType.BANNER, AdServer.UNITY, errorInfo?.errorMessage)
            }

            override fun onBannerLeftApplication(bannerView: BannerView?) {
            }
        }
    }

    override fun load() {
        adView.load()
        adLayout.removeAllViews()
        adLayout.addView(adView)
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun destroy() {
        adView.destroy()
    }
}