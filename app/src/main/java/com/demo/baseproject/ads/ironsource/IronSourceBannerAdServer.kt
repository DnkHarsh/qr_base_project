package com.demo.baseproject.ads.ironsource

import android.app.Activity
import android.view.ViewGroup
import com.demo.baseproject.ads.AdServer
import com.demo.baseproject.ads.AdType
import com.demo.baseproject.ads.listeners.AdStatusListener
import com.demo.baseproject.ads.listeners.BannerAdListener
import com.demo.baseproject.events.EventLogger
import com.ironsource.mediationsdk.ISBannerSize
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.IronSourceBannerLayout
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.LevelPlayBannerListener


/**
 * A class that serves a IronSource BannerAd [adView]. All the methods in this class are self-explanatory, hence not documented
 */

class IronSourceBannerAdServer(
    private val activity: Activity,
    private val adLayout: ViewGroup,
    private val adUnitId: String,
    private val adStatusListener: AdStatusListener?
) : BannerAdListener {
    private var adView: IronSourceBannerLayout? = null

    init {
        adView = IronSource.createBanner(activity, ISBannerSize.BANNER)
    }

    override fun load() {
        IronSource.loadBanner(adView, adUnitId)
        adView?.levelPlayBannerListener = object : LevelPlayBannerListener {
            override fun onAdLoaded(p0: AdInfo?) {
                adStatusListener?.onAdLoaded()
                EventLogger.logAdLoaded(AdType.BANNER, AdServer.IRONSOURCE)

                adLayout.removeAllViews()
                adLayout.addView(adView)
            }

            override fun onAdLoadFailed(p0: IronSourceError?) {
                adStatusListener?.onAdFailed(p0?.errorMessage)
                EventLogger.logAdFailed(AdType.BANNER, AdServer.IRONSOURCE, p0?.errorMessage)
            }

            override fun onAdClicked(p0: AdInfo?) {
            }

            override fun onAdLeftApplication(p0: AdInfo?) {
            }

            override fun onAdScreenPresented(p0: AdInfo?) {
                adStatusListener?.onAdImpression()
                EventLogger.logAdImpression(AdType.BANNER, AdServer.IRONSOURCE)
            }

            override fun onAdScreenDismissed(p0: AdInfo?) {
            }
        }
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun destroy() {
        IronSource.destroyBanner(adView)
    }
}