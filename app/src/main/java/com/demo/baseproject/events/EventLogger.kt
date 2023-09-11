package com.demo.baseproject.events

import android.content.Context
import android.os.Bundle
import com.demo.baseproject.BuildConfig
import com.demo.baseproject.ads.AdServer
import com.demo.baseproject.ads.AdType
import com.demo.baseproject.utils.extensions.getCountryBasedOnSimCardOrNetwork
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class EventLogger @Inject constructor(@ApplicationContext private val context: Context) {

    private val analytics = Firebase.analytics

    fun logScreenLoad(screenName: String) {
        if (isReleaseBuild) {
            val bundle = addBasicParams().apply {
                putString("screen_name", screenName)
            }
            analytics.logEvent("screen_load", bundle)
        }
    }

    fun logViewClick(viewName: String, screenName: String) {
        if (isReleaseBuild) {
            val bundle = addBasicParams().apply {
                putString("view_name", viewName)
                putString("screen_name", screenName)
            }
            analytics.logEvent("view_click", bundle)
        }
    }

    fun logAdLoaded(adType: AdType, adServer: AdServer) {
        if (isReleaseBuild) {
            val bundle = addBasicParams().apply {
                putString("ad_type", adType.name)
                putString("ad_server", adServer.name)
            }
            analytics.logEvent("ad_loaded", bundle)
        }
    }

    fun logAdFailed(adType: AdType, adServer: AdServer, message: String?) {
        if (isReleaseBuild) {
            val bundle = addBasicParams().apply {
                putString("ad_type", adType.name)
                putString("ad_server", adServer.name)
                putString("error_message", message)
            }
            analytics.logEvent("ad_failed", bundle)
        }
    }

    fun logAdImpression(adType: AdType, adServer: AdServer) {
        if (isReleaseBuild) {
            val bundle = addBasicParams().apply {
                putString("ad_type", adType.name)
                putString("ad_server", adServer.name)
            }
            analytics.logEvent("ad_impression", bundle)
        }
    }

    fun logAdRewardEarned(adType: AdType, adServer: AdServer) {
        if (isReleaseBuild) {
            val bundle = addBasicParams().apply {
                putString("ad_type", adType.name)
                putString("ad_server", adServer.name)
            }
            analytics.logEvent("ad_reward_earned", bundle)
        }
    }

    fun logPremiumPurchased() {
        if (isReleaseBuild) {
            val bundle = addBasicParams()
            analytics.logEvent("premium_purchased", bundle)
        }
    }

    private val isReleaseBuild = BuildConfig.DEBUG.not() && BuildConfig.FLAVOR == "signed"

    private fun addBasicParams(): Bundle {
        return Bundle().apply {
            putString("locale", context.getCountryBasedOnSimCardOrNetwork())
        }
    }
}