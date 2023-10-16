package com.demo.baseproject.ads.admob

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.demo.baseproject.ads.AdServer
import com.demo.baseproject.ads.AdType
import com.demo.baseproject.ads.TemplateType
import com.demo.baseproject.ads.listeners.AdStatusListener
import com.demo.baseproject.ads.listeners.BannerAdListener
import com.demo.baseproject.databinding.NativeAdCarouselCardTemplateLayoutBinding
import com.demo.baseproject.databinding.NativeAdListCardTemplateLayoutBinding
import com.demo.baseproject.databinding.NativeAdPauseTemplateLayoutBinding
import com.demo.baseproject.databinding.NativeAdSmallTemplateLayoutBinding
import com.demo.baseproject.databinding.NativeAdStaticPlacementCardTemplateLayoutBinding
import com.demo.baseproject.events.EventLogger
import com.demo.baseproject.utils.extensions.visible
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions

/**
 * A class that serves a Admob NativeAd [mAdView]. All the methods in this class are self-explanatory
 */
class AdmobNativeAdServer(
    private val ctx: Activity,
    private val adLayout: ViewGroup,
    private val adUnitId: String,
    private val templateType: TemplateType,
    private val eventLogger: EventLogger,
    private val adStatusListener: AdStatusListener?
) : BannerAdListener {
    private var adTemplate: TemplateView? = null
    private lateinit var adLoader: AdLoader
    private var mAdView: NativeAd? = null

    init {
        initAdAndSetListener()
    }

    private fun initAdAndSetListener() {
        adTemplate = when (templateType) {
            TemplateType.SMALL -> {
                val binding = NativeAdSmallTemplateLayoutBinding.inflate(LayoutInflater.from(ctx))
                binding.myTemplate
            }

            TemplateType.CAROUSEL_CARD -> {
                val binding =
                    NativeAdCarouselCardTemplateLayoutBinding.inflate(LayoutInflater.from(ctx))
                binding.myTemplate
            }

            TemplateType.LIST_CARD -> {
                val binding =
                    NativeAdListCardTemplateLayoutBinding.inflate(LayoutInflater.from(ctx))
                binding.myTemplate
            }

            TemplateType.PLAY_PAUSE_CARD -> {
                val binding = NativeAdPauseTemplateLayoutBinding.inflate(LayoutInflater.from(ctx))
                binding.myTemplate
            }

            TemplateType.STATIC_PLACEMENT_CARD -> {
                val binding =
                    NativeAdStaticPlacementCardTemplateLayoutBinding.inflate(LayoutInflater.from(ctx))
                binding.myTemplate
            }

        }
        adLoader = AdLoader.Builder(ctx, adUnitId)
            .forNativeAd { nativeAd ->

                if (ctx.isDestroyed) {
                    nativeAd.destroy()
                    return@forNativeAd
                }

                val styles = NativeTemplateStyle.Builder().withMainBackgroundColor(
                    ColorDrawable(Color.TRANSPARENT)
                ).build()

                mAdView = nativeAd
                adTemplate?.setStyles(styles)
                adTemplate?.setNativeAd(nativeAd)
                adTemplate?.visible()

            }.withAdListener(object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    adLayout.removeAllViews()
                    adLayout.addView(adTemplate)
                    adStatusListener?.onAdLoaded()
                    eventLogger.logAdLoaded(AdType.NATIVE, AdServer.ADMOB)
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    adStatusListener?.onAdFailed(adError.message)
                    eventLogger.logAdFailed(AdType.NATIVE, AdServer.ADMOB, adError.message)
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    adStatusListener?.onAdImpression()
                    eventLogger.logAdImpression(AdType.NATIVE, AdServer.ADMOB)
                }
            }).withNativeAdOptions(
                NativeAdOptions.Builder().build()
            )
            .build()


    }

    override fun load() {
        adLoader.loadAd(AdRequest.Builder().build())
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun destroy() {
        mAdView?.destroy()
        adTemplate?.destroyNativeAd()
    }
}