package com.demo.baseproject.application

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.MutableLiveData
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.demo.baseproject.ads.AdResponse
import com.demo.baseproject.network.NetworkConstants
import com.demo.baseproject.storage.AppPref
import com.demo.baseproject.utils.extensions.setBaseWindowDimensions
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : MultiDexApplication() {

    @Inject
    lateinit var firebaseRemoteConfig: FirebaseRemoteConfig

    @Inject
    lateinit var gson: Gson

    companion object {
        lateinit var instance: BaseApplication
        var isAppWentToBg = true
        var isPurchasedPremiumInThisSession = false
        var adResponse = MutableLiveData<AdResponse?>()
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

        observeNetworkChange()
    }

    private fun observeNetworkChange() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                if (adResponse.value == null) {
                    firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {
                        if (adResponse.value == null) {
                            val response = try {
                                gson.fromJson(
                                    firebaseRemoteConfig.getString(NetworkConstants.APP_ADS),
                                    AdResponse::class.java
                                )
                            } catch (e: Exception) {
                                null
                            }
                            adResponse.postValue(response)
                        }
                    }
                }
            }

            override fun onLost(network: Network) {
            }
        })
    }

    private fun increaseSessionCount() {
        var count = AppPref.getInstance().getValue(AppPref.SESSION_COUNT, 0)
        AppPref.getInstance().setValue(AppPref.SESSION_COUNT, ++count)
    }

    private fun initFirebaseRemoteConfig() {
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {}
    }
}
