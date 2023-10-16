package com.demo.baseproject.ads.gam

import android.content.Context
import android.view.ViewGroup
import com.demo.baseproject.ads.AdServer
import com.demo.baseproject.ads.AdType
import com.demo.baseproject.ads.listeners.AdStatusListener
import com.demo.baseproject.ads.listeners.BannerAdListener
import com.demo.baseproject.events.EventLogger
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView

/**
 * A class that serves a GAM BannerAd [adView]. All the methods in this class are self-explanatory, hence not documented
 */
class GamBannerAdServer(
    private val ctx: Context,
    private val adLayout: ViewGroup,
    private val adUnitId: String,
    private val eventLogger: EventLogger,
    private val adStatusListener: AdStatusListener?
) : BannerAdListener {

    private var adView = AdManagerAdView(ctx)
    private var adRequest: AdManagerAdRequest

    init {
        adView.setAdSize(AdSize.BANNER)
        adView.adUnitId = adUnitId
        adRequest = AdManagerAdRequest.Builder().build()
    }

    override fun load() {
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                adLayout.removeAllViews()
                adLayout.addView(adView)
                adStatusListener?.onAdLoaded()
                eventLogger.logAdLoaded(AdType.BANNER, AdServer.GAM)
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                adStatusListener?.onAdFailed(p0.message)
                eventLogger.logAdFailed(AdType.BANNER, AdServer.GAM, p0.message)
            }

            override fun onAdImpression() {
                super.onAdImpression()
                adStatusListener?.onAdImpression()
                eventLogger.logAdImpression(AdType.BANNER, AdServer.GAM)
            }
        }
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