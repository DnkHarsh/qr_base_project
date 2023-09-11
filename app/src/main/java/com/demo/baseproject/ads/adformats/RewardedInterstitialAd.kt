package com.demo.baseproject.ads.adformats

import android.app.Activity
import com.demo.baseproject.ads.AdPlacementResponse
import com.demo.baseproject.ads.AdServer
import com.demo.baseproject.ads.admob.AdmobRewardedInterstitialAdServer
import com.demo.baseproject.ads.gam.GamRewardedInterstitialAdServer
import com.demo.baseproject.ads.listeners.AdStatusListener
import com.demo.baseproject.ads.listeners.FullScreenAdListener
import com.demo.baseproject.events.EventLogger

class RewardedInterstitialAd(
    private val activity: Activity,
    private val adPlacementResponse: AdPlacementResponse,
    private val eventLogger: EventLogger,
    private val adStatusListener: AdStatusListener
) {
    private var adListener: FullScreenAdListener? = null

    init {
        initAdServer()
    }

    private fun initAdServer() {
        adListener = when (adPlacementResponse.adServer) {
            AdServer.ADMOB -> AdmobRewardedInterstitialAdServer(
                activity,
                adPlacementResponse.adUnitId,
                eventLogger,
                adStatusListener
            )

            AdServer.GAM -> GamRewardedInterstitialAdServer(
                activity,
                adPlacementResponse.adUnitId,
                eventLogger,
                adStatusListener
            )

            else -> {
                GamRewardedInterstitialAdServer(
                    activity,
                    adPlacementResponse.adUnitId,
                    eventLogger,
                    adStatusListener
                )
            }
        }
    }

    fun show() {
        adListener?.show()
    }
}