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

class InterstitialAd(
    private val activity: Activity,
    private val adPlacementResponse: AdPlacementResponse,
    private val adStatusListener: AdStatusListener
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
                adStatusListener
            )

            AdServer.GAM -> GamInterstitialAdServer(
                activity,
                adPlacementResponse.adUnitId,
                adStatusListener
            )

            AdServer.IRONSOURCE -> IronSourceInterstitialAdServer(
                adPlacementResponse.adUnitId,
                adStatusListener
            )

            AdServer.INMOBI -> InMobiInterstitialAdServer(
                activity,
                adPlacementResponse.adUnitId,
                adStatusListener
            )

            AdServer.UNITY -> UnityInterstitialAdServer(
                activity,
                adPlacementResponse.adUnitId,
                adStatusListener
            )
        }
    }

    fun show() {
        adListener?.show()
    }
}