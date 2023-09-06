package com.demo.baseproject.ads.inmobi

import android.content.Context
import android.view.ViewGroup
import com.demo.baseproject.ads.AdServer
import com.demo.baseproject.ads.AdType
import com.demo.baseproject.ads.listeners.AdStatusListener
import com.demo.baseproject.ads.listeners.BannerAdListener
import com.demo.baseproject.events.EventLogger
import com.inmobi.ads.AdMetaInfo
import com.inmobi.ads.InMobiAdRequestStatus
import com.inmobi.ads.InMobiBanner
import com.inmobi.ads.listeners.BannerAdEventListener

class InMobiBannerAdServer(
    private val ctx: Context,
    private val adLayout: ViewGroup,
    private val adUnitId: String,
    private val adStatusListener: AdStatusListener?
) : BannerAdListener {

    private val adView = InMobiBanner(ctx, adUnitId.toLong())

    init {
        adView.setEnableAutoRefresh(true)
        adView.setRefreshInterval(60)
        adView.setAnimationType(InMobiBanner.AnimationType.ANIMATION_ALPHA)
        adView.setListener(object : BannerAdEventListener() {
            override fun onAdFetchFailed(p0: InMobiBanner, p1: InMobiAdRequestStatus) {
                super.onAdFetchFailed(p0, p1)
                adStatusListener?.onAdFailed(p1.message)
                EventLogger.logAdFailed(AdType.BANNER, AdServer.INMOBI, p1.message)
            }

            override fun onAdLoadSucceeded(p0: InMobiBanner, p1: AdMetaInfo) {
                super.onAdLoadSucceeded(p0, p1)
                adStatusListener?.onAdLoaded()
                EventLogger.logAdLoaded(AdType.BANNER, AdServer.INMOBI)
            }

            override fun onAdLoadFailed(p0: InMobiBanner, p1: InMobiAdRequestStatus) {
                super.onAdLoadFailed(p0, p1)
                adStatusListener?.onAdFailed(p1.message)
                EventLogger.logAdFailed(AdType.BANNER, AdServer.INMOBI, p1.message)
            }

            override fun onAdImpression(p0: InMobiBanner) {
                super.onAdImpression(p0)
                adStatusListener?.onAdImpression()
                EventLogger.logAdImpression(AdType.BANNER, AdServer.INMOBI)
            }
        })
    }

    override fun load() {
        adView.load()
        adLayout.removeAllViews()
        adLayout.addView(adView)
    }

    override fun pause() {
        adView.pause()
    }

    override fun resume() {
        adView.resume()
    }

    override fun destroy() {
        adView.destroy()
    }
}