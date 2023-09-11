package com.demo.baseproject.ads.adformats

import android.app.Activity
import com.demo.baseproject.ads.AdPlacementResponse
import com.demo.baseproject.ads.AdServer
import com.demo.baseproject.ads.admob.AdmobInterstitialAdServer
import com.demo.baseproject.ads.gam.GamInterstitialAdServer
import com.demo.baseproject.ads.inmobi.InMobiInterstitialAdServer
import com.demo.baseproject.ads.ironsource.IronSourceInterstitialAdServer
import com.demo.baseproject.ads.listeners.AdStatusListener
import com.demo.baseproject.ads.listeners.FullScreenAdListener
import com.demo.baseproject.ads.unity.UnityInterstitialAdServer
import com.demo.baseproject.events.EventLogger

class InterstitialAd(
    private val activity: Activity,
    private val adPlacementResponse: AdPlacementResponse,
    private val eventLogger: EventLogger,
    private val adStatusListener: AdStatusListener? = null
) {
    private var adListener: FullScreenAdListener? = null

    init {
        initAdServer()
    }

    private fun initAdServer() {
        adListener = when (adPlacementResponse.adServer) {
            AdServer.ADMOB -> AdmobInterstitialAdServer(
                activity,
                adPlacementResponse.adUnitId,
                eventLogger,
                adStatusListener
            )

            AdServer.GAM -> GamInterstitialAdServer(
                activity,
                adPlacementResponse.adUnitId,
                eventLogger,
                adStatusListener
            )

            AdServer.IRONSOURCE -> IronSourceInterstitialAdServer(
                adPlacementResponse.adUnitId,
                eventLogger,
                adStatusListener
            )

            AdServer.INMOBI -> InMobiInterstitialAdServer(
                activity,
                adPlacementResponse.adUnitId,
                eventLogger,
                adStatusListener
            )

            AdServer.UNITY -> UnityInterstitialAdServer(
                activity,
                adPlacementResponse.adUnitId,
                eventLogger,
                adStatusListener
            )
        }
    }

    fun show() {
        adListener?.show()
    }
}