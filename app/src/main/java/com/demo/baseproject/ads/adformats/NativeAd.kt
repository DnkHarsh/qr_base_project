package com.demo.baseproject.ads.adformats

import android.app.Activity
import android.view.ViewGroup
import com.demo.baseproject.ads.AdPlacementResponse
import com.demo.baseproject.ads.AdServer
import com.demo.baseproject.ads.admob.AdmobNativeAdServer
import com.demo.baseproject.ads.gam.GamNativeAdServer
import com.demo.baseproject.ads.listeners.AdStatusListener
import com.demo.baseproject.ads.listeners.BannerAdListener
import com.demo.baseproject.events.EventLogger

class NativeAd(
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
            AdServer.ADMOB -> AdmobNativeAdServer(
                activity,
                adLayout,
                adPlacementResponse.adUnitId,
                adPlacementResponse.templateType!!,
                eventLogger,
                adStatusListener
            )

            AdServer.GAM -> GamNativeAdServer(
                activity,
                adLayout,
                adPlacementResponse.adUnitId,
                adPlacementResponse.templateType!!,
                eventLogger,
                adStatusListener
            )

            else -> {
                AdmobNativeAdServer(
                    activity,
                    adLayout,
                    adPlacementResponse.adUnitId,
                    adPlacementResponse.templateType!!,
                    eventLogger,
                    adStatusListener
                )
            }
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