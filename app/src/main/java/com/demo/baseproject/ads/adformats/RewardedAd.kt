package com.demo.baseproject.ads.adformats

import android.app.Activity
import com.demo.baseproject.ads.AdPlacementResponse
import com.demo.baseproject.ads.AdServer
import com.demo.baseproject.ads.admob.AdmobRewardedVideoAdServer
import com.demo.baseproject.ads.gam.GamRewardedVideoAdServer
import com.demo.baseproject.ads.inmobi.InMobiRewardedAdServer
import com.demo.baseproject.ads.ironsource.IronSourceRewardedAdServer
import com.demo.baseproject.ads.listeners.AdStatusListener
import com.demo.baseproject.ads.listeners.FullScreenAdListener
import com.demo.baseproject.ads.unity.UnityRewardedAdServer

class RewardedAd(
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
            AdServer.ADMOB -> AdmobRewardedVideoAdServer(
                activity,
                adPlacementResponse.adUnitId,
                adStatusListener
            )

            AdServer.GAM -> GamRewardedVideoAdServer(
                activity,
                adPlacementResponse.adUnitId,
                adStatusListener
            )

            AdServer.IRONSOURCE -> IronSourceRewardedAdServer(
                adPlacementResponse.adUnitId,
                adStatusListener
            )

            AdServer.INMOBI -> InMobiRewardedAdServer(
                activity,
                adPlacementResponse.adUnitId,
                adStatusListener
            )

            AdServer.UNITY -> UnityRewardedAdServer(
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