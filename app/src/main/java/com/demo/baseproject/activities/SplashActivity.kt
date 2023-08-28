package com.demo.baseproject.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.demo.baseproject.R
import com.demo.baseproject.storage.AppPref
import com.demo.baseproject.utils.extensions.isConnectedToInternet
import com.demo.baseproject.utils.extensions.showToast
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var firebaseRemoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { true }
        if (isConnectedToInternet()) {
            lifecycleScope.launch {
//                firebaseRemoteConfig.fetchAndActivate().await()
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
        } else {
            showToast(getString(R.string.no_connection))
        }
    }

    private fun subscribeNotificationTopic(isPaidUser: Boolean) {
        if (isPaidUser) {
            if (AppPref.getInstance().getValue(AppPref.SUBSCRIBED_NOTIFICATION_TOPIC, false)) {
                Firebase.messaging.subscribeToTopic("paid")
                Firebase.messaging.unsubscribeFromTopic("free")
                AppPref.getInstance().setValue(AppPref.SUBSCRIBED_NOTIFICATION_TOPIC, true)
            }
        } else {
            if (AppPref.getInstance().getValue(AppPref.SUBSCRIBED_NOTIFICATION_TOPIC, false)) {
                Firebase.messaging.subscribeToTopic("free")
                Firebase.messaging.unsubscribeFromTopic("paid")
                AppPref.getInstance().setValue(AppPref.SUBSCRIBED_NOTIFICATION_TOPIC, true)
            }
        }
    }
}

