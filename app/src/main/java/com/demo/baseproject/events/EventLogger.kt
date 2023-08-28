package com.demo.baseproject.events

import android.os.Bundle
import com.demo.baseproject.ads.AdServer
import com.demo.baseproject.ads.AdType
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object EventLogger {

    private val analytics = Firebase.analytics

    fun logScreenLoad(screenName: String) {
        val bundle = Bundle().apply {
            putString("screen_name", screenName)
        }
        analytics.logEvent("screen_load", bundle)
    }

    fun logViewClick(viewName: String, screenName: String) {
        val bundle = Bundle().apply {
            putString("view_name", viewName)
            putString("screen_name", screenName)
        }
        analytics.logEvent("view_click", bundle)
    }

    fun logAdLoaded(adType: AdType, adServer: AdServer) {
        val bundle = Bundle().apply {
            putString("ad_type", adType.name)
            putString("ad_server", adServer.name)
        }
        analytics.logEvent("ad_loaded", bundle)
    }

    fun logAdFailed(adType: AdType, adServer: AdServer, message: String?) {
        val bundle = Bundle().apply {
            putString("ad_type", adType.name)
            putString("ad_server", adServer.name)
            putString("error_message", message)
        }
        analytics.logEvent("ad_failed", bundle)
    }

    fun logAdImpression(adType: AdType, adServer: AdServer) {
        val bundle = Bundle().apply {
            putString("ad_type", adType.name)
            putString("ad_server", adServer.name)
        }
        analytics.logEvent("ad_impression", bundle)
    }

    fun logAdRewardEarned(adType: AdType, adServer: AdServer) {
        val bundle = Bundle().apply {
            putString("ad_type", adType.name)
            putString("ad_server", adServer.name)
        }
        analytics.logEvent("ad_reward_earned", bundle)
    }

    fun logPremiumPurchased() {
        val bundle = Bundle()
        analytics.logEvent("ad_reward_earned", bundle)
    }
}