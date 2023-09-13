package com.demo.baseproject.application

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.demo.baseproject.ads.AdResponse
import com.demo.baseproject.storage.AppPref
import com.demo.baseproject.utils.extensions.setBaseWindowDimensions
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : MultiDexApplication() {

    @Inject
    lateinit var firebaseRemoteConfig: FirebaseRemoteConfig

    companion object {
        lateinit var instance: BaseApplication
        var isAppWentToBg = true
        var isPurchasedPremiumInThisSession = false
        var adResponse: AdResponse? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        MultiDex.install(this)
        AppPref.initialize(this)
        setBaseWindowDimensions(this)
        initFirebaseRemoteConfig()
        increaseSessionCount()
        if (AppPref.getInstance().getValue(AppPref.SHOW_IN_APP_REVIEW_AFTER_SESSIONS, 0) == 0) {
            AppPref.getInstance().setValue(AppPref.SHOW_IN_APP_REVIEW_AFTER_SESSIONS, 10)
        }
    }

    private fun increaseSessionCount() {
        var count = AppPref.getInstance().getValue(AppPref.SESSION_COUNT, 0)
        AppPref.getInstance().setValue(AppPref.SESSION_COUNT, ++count)
    }

    private fun initFirebaseRemoteConfig() {
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {}
    }
}
