package com.demo.baseproject.ads.ironsource

import com.demo.baseproject.ads.AdServer
import com.demo.baseproject.ads.AdType
import com.demo.baseproject.ads.listeners.AdStatusListener
import com.demo.baseproject.ads.listeners.FullScreenAdListener
import com.demo.baseproject.events.EventLogger
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.LevelPlayInterstitialListener
import javax.inject.Inject

/**
 * A class that serves a Iron Source Interstitial Ads. All the methods in this class are self-explanatory, hence not documented.
 */
class IronSourceInterstitialAdServer(
    private val adUnitId: String,
    private val eventLogger: EventLogger,
    private val adStatusListener: AdStatusListener?
) : FullScreenAdListener {

    init {
        initAdAndSetListener()
    }

    private fun initAdAndSetListener() {
        IronSource.setLevelPlayInterstitialListener(object : LevelPlayInterstitialListener {
            override fun onAdReady(p0: AdInfo?) {
                adStatusListener?.onAdLoaded()
                eventLogger.logAdLoaded(AdType.INTERSTITIAL, AdServer.IRONSOURCE)
            }

            override fun onAdLoadFailed(p0: IronSourceError?) {
                adStatusListener?.onAdFailed(p0?.errorMessage)
                eventLogger.logAdFailed(AdType.INTERSTITIAL, AdServer.IRONSOURCE, p0?.errorMessage)
            }

            override fun onAdOpened(p0: AdInfo?) {
            }

            override fun onAdShowSucceeded(p0: AdInfo?) {
                adStatusListener?.onAdImpression()
                eventLogger.logAdImpression(AdType.INTERSTITIAL, AdServer.IRONSOURCE)
            }

            override fun onAdShowFailed(p0: IronSourceError?, p1: AdInfo?) {
                adStatusListener?.onAdFailed(p0?.errorMessage)
                eventLogger.logAdFailed(AdType.INTERSTITIAL, AdServer.IRONSOURCE, p0?.errorMessage)
            }

            override fun onAdClicked(p0: AdInfo?) {
            }

            override fun onAdClosed(p0: AdInfo?) {
                adStatusListener?.onDismissFullScreenAd()
            }
        })
        IronSource.loadInterstitial()
    }

    override fun show() {
        if (IronSource.isInterstitialReady()) {
            IronSource.showInterstitial(adUnitId)
        }
    }
}