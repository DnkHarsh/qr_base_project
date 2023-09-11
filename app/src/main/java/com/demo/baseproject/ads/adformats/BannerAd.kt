package com.demo.baseproject.ads.adformats

import android.app.Activity
import android.view.ViewGroup
import com.demo.baseproject.ads.AdPlacementResponse
import com.demo.baseproject.ads.AdServer
import com.demo.baseproject.ads.admob.AdmobBannerAdServer
import com.demo.baseproject.ads.gam.GamBannerAdServer
import com.demo.baseproject.ads.inmobi.InMobiBannerAdServer
import com.demo.baseproject.ads.ironsource.IronSourceBannerAdServer
import com.demo.baseproject.ads.listeners.AdStatusListener
import com.demo.baseproject.ads.listeners.BannerAdListener
import com.demo.baseproject.ads.unity.UnityBannerAdServer
import com.demo.baseproject.events.EventLogger

class BannerAd(
    private val activity: Activity,
    private val adLayout: ViewGroup,
    private val adPlacementResponse: AdPlacementResponse,
    private val eventLogger: EventLogger,
    private val adStatusListener: AdStatusListener? = null
) {
    private var adListener: BannerAdListener? = null

    init {
        initAdServer()
    }

    private fun initAdServer() {
        adListener = when (adPlacementResponse.adServer) {
            AdServer.ADMOB -> AdmobBannerAdServer(
                activity,
                adLayout,
                adPlacementResponse.adUnitId,
                eventLogger,
                adStatusListener
            )

            AdServer.GAM -> GamBannerAdServer(
                activity,
                adLayout,
                adPlacementResponse.adUnitId,
                eventLogger,
                adStatusListener
            )

            AdServer.IRONSOURCE -> IronSourceBannerAdServer(
                activity,
                adLayout,
                adPlacementResponse.adUnitId,
                eventLogger,
                adStatusListener
            )

            AdServer.INMOBI -> InMobiBannerAdServer(
                activity,
                adLayout,
                adPlacementResponse.adUnitId,
                eventLogger,
                adStatusListener
            )

            AdServer.UNITY -> UnityBannerAdServer(
                activity, adLayout, adPlacementResponse.adUnitId,
                eventLogger,
                adStatusListener
            )
        }
    }

    fun load() {
        adListener?.load()
    }

    fun pause() {
        adListener?.pause()
    }

    fun resume() {
        adListener?.resume()
    }

    fun destroy() {
        adListener?.destroy()
        adLayout.removeAllViews()
    }
}