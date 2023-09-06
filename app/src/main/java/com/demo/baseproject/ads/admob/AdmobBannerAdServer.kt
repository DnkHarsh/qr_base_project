package com.demo.baseproject.ads.admob

import android.content.Context
import android.view.ViewGroup
import com.demo.baseproject.ads.AdServer
import com.demo.baseproject.ads.AdType
import com.demo.baseproject.ads.listeners.AdStatusListener
import com.demo.baseproject.ads.listeners.BannerAdListener
import com.demo.baseproject.events.EventLogger
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

/**
 * A class that serves a Admob Banner Ads. All the methods in this class are self-explanatory, hence not documented.
 */
class AdmobBannerAdServer(
    private val ctx: Context,
    private val adLayout: ViewGroup,
    private val adUnitId: String,
    private val adStatusListener: AdStatusListener?
) : BannerAdListener {
    private var adView: AdView = AdView(ctx)
    private var adRequest: AdRequest

    init {
        adView.setAdSize(AdSize.BANNER)
        adView.adUnitId = adUnitId
        adRequest = AdRequest.Builder().build()
    }

    override fun load() {
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                adStatusListener?.onAdLoaded()
                EventLogger.logAdLoaded(AdType.BANNER, AdServer.ADMOB)

                adLayout.removeAllViews()
                adLayout.addView(adView)
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                adStatusListener?.onAdFailed(p0.message)
                EventLogger.logAdFailed(AdType.BANNER, AdServer.ADMOB, p0.message)
            }

            override fun onAdImpression() {
                super.onAdImpression()
                adStatusListener?.onAdImpression()
                EventLogger.logAdImpression(AdType.BANNER, AdServer.ADMOB)
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