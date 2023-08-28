package com.demo.baseproject.notification.push.service


import android.content.Intent
import com.demo.baseproject.activities.SplashActivity
import com.demo.baseproject.application.BaseApplication
import com.demo.baseproject.utils.extensions.showNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Date
import java.util.Random


class FCMIntentService : FirebaseMessagingService() {
    private var NOTIFICATION_ID = 0
    private var channelId: String? = null

    /**
     * Called when message is received.
     */
    override fun onMessageReceived(message: RemoteMessage) {

        channelId = packageName + "PUSH"
        NOTIFICATION_ID = try {
            (Date().time / 1000L % Integer.MAX_VALUE).toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            val random = Random()
            random.nextInt(9999 - 1000) + 10
        }

        sendOtherNotification(message)
    }

    private fun sendOtherNotification(remoteMessage: RemoteMessage) {
        var notificationIntent: Intent? = null
        remoteMessage.notification ?: run {
            return
        }
        val title = remoteMessage.notification?.title
        val message: String? = remoteMessage.notification?.body
        var isAppRunningInBackGround = true

        try {
            isAppRunningInBackGround = BaseApplication.isAppWentToBg
        } catch (e: ExceptionInInitializerError) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: NoClassDefFoundError) {
            e.printStackTrace()
        }

        if (isAppRunningInBackGround) {
            notificationIntent = Intent(applicationContext, SplashActivity::class.java)
        }

        val contentIntent: Intent = notificationIntent ?: Intent()
        channelId?.let {
            title?.let { it1 ->
                message?.let { it2 ->
                    showNotification(
                        it,
                        NOTIFICATION_ID,
                        it1,
                        it2,
                        contentIntent
                    )
                }
            }
        }
    }
}