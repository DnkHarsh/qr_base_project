package com.demo.baseproject.utils.extensions

import android.app.*
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.graphics.Color
import android.media.RingtoneManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.demo.baseproject.R
import com.demo.baseproject.ui.activities.SplashActivity
import com.demo.baseproject.utils.logger.infoLog
import java.util.Date
import java.util.Random

/**
 * This method is used set window dimensions
 *
 * @param context context of current screen.
 */
fun setBaseWindowDimensions(context: Context) {
    val displayMetrics = context.resources.displayMetrics
    SCREEN_HEIGHT = displayMetrics.heightPixels
    SCREEN_WIDTH = displayMetrics.widthPixels
}

/**
 * Method to check runnable device is virtual device ot not.
 *
 * @return true if it is virtual device else false.
 */
@Suppress("unused")
fun isRunningAppInEmulator(): Boolean {
    try {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.MANUFACTURER.contains("unknown")
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || Build.PRODUCT == "google_sdk"
                || Build.PRODUCT == "sdk_google"
                || Build.HARDWARE.contains("golfdish"))
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }

}

/**
 * Method to share single/multiple images
 */
@Suppress("unused")
fun Context.shareImg(singleFile: Uri?, multipleFiles: ArrayList<Uri>?) {
    try {
        var share: Intent? = null
        when {
            singleFile != null -> {
                share = generateShareIntentForSingleImage(singleFile)
            }

            multipleFiles != null -> {
                if (multipleFiles.size == 1) {
                    share = generateShareIntentForSingleImage(multipleFiles[0])
                } else {
                    share = Intent(Intent.ACTION_SEND_MULTIPLE)
                    share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, multipleFiles)
                }
            }
        }
        share?.type = "image/*"
        startActivity(Intent.createChooser(share, getString(R.string.share_image_msg)))
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

/**
 * This method generates share intent for single image
 * @param singleFile is file uri
 * @return intent
 */
private fun Context.generateShareIntentForSingleImage(singleFile: Uri): Intent {
    val share = Intent(Intent.ACTION_SEND)
    share.putExtra(
        Intent.EXTRA_TEXT,
        getString(R.string.share_image_msg) + " " + "https://play.google.com/store/apps/details?id=" + packageName
    )
    share.putExtra(Intent.EXTRA_STREAM, singleFile)
    return share
}

/**
 * method to check service is running or not
 * @param serviceClass service name
 * @return true/false
 */
@Suppress("unused", "DEPRECATION")
fun Context.isMyServiceRunning(serviceClass: Class<*>): Boolean {
    var isServiceRunning = false
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    //RunningServiceInfo class returns running all services in background with extra data.
    for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
        if (serviceClass.name == service.service.className) {
            //Our notification service is running, so no need to restart it.
            isServiceRunning = true
            break
        }
    }
    return isServiceRunning
}

/**
 * Method to check internet connection.
 * @return true/false
 */
fun Context.isConnectedToInternet(): Boolean {
    try {
        var isInternet = false
        try {
            val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            isInternet =
                capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return isInternet
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

/**
 * Method to navigate playstore with user entered package name
 * @param packageName package name what will navigate in playstore based on this package name
 */
@Suppress("unused")
fun Context.navigateToPlayStoreWithSpecificPackage(packageName: String) {
    try {
        val uri = Uri.parse("market://details?id=$packageName&hl=en")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        startActivity(goToMarket)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }

}


/**
 * This method is used convert window dimensions pixel to Density Pixel
 * @param size if pass SCREEN_HEIGHT it return screen height in DP else return Screen Width in DP.
 */
@Suppress("unused")
fun Context.convertWindowDimensionsInDP(size: Int): Float {
    val displayMetrics = resources.displayMetrics
    return if (size == SCREEN_HEIGHT) {
        displayMetrics.heightPixels / displayMetrics.density
    } else {
        displayMetrics.widthPixels / displayMetrics.density

    }

}


/**
 * This method is used hide keyboard
 */
@Suppress("unused")
fun Activity.hideKeyboard() {
    try {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus ?: run {
            return
        }
        inputManager.hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

/**
 * This method is used show keyboard
 */
@Suppress("unused")
fun Activity.showKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    currentFocus ?: run {
        return
    }
    inputMethodManager.showSoftInput(currentFocus, InputMethodManager.SHOW_FORCED)
}

/**
 * This method is used show keyboard
 */
@Suppress("unused")
fun Activity.showKeyboard(view: View?) {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}


/**
 * This method is used hide keyboard
 * @param view     instance of view
 */
@Suppress("unused")
fun Activity.hideKeyboard(view: View?) {
    try {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view?.windowToken, 0)

    } catch (e: Exception) {
        infoLog("KeyBoardUtil", e.toString())
    }

}

/**
 * Method to code text in clip board
 *

 * @param text    text what wan to copy in clipboard
 * @param label   label what want to copied
 */
@Suppress("unused")
fun Context?.copyCodeInClipBoard(text: String, label: String) {
    this?.let {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text) ?: return
        clipboard.setPrimaryClip(clip)
    }
}


/**
 * Method to check third party application is installed in device or not.
 * @param packageName Third party application package name to check is it available in device.
 * @return true if requested package name is installed in device else false
 */
@Suppress("unused")
fun Activity.isAppInstalled(packageName: String): Boolean {
    val pm = packageManager
    val installed: Boolean = try {
        pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: NameNotFoundException) {
        false
    }

    return installed
}

/**
 * Method to rate application
 */
fun Context.rateApp() {
    val uri = Uri.parse("market://details?id=$packageName")
    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
    // To count with Play market backstack, After pressing back button,
    // to taken back to our application, we need to add following flags to intent.
    goToMarket.addFlags(
        Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK
    )
    try {
        startActivity(goToMarket)
    } catch (e: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }

}

/**
 * Method to share application
 *
 * @param shareText text to be shared with app link
 */
fun Context.shareApp(shareText: String) {
    val sendIntent = Intent()
    sendIntent.action = Intent.ACTION_SEND
    sendIntent.putExtra(
        Intent.EXTRA_TEXT, shareText + "\n\n" +
                "https://play.google.com/store/apps/details?id=" + packageName
    )
    sendIntent.type = "text/plain"
    startActivity(Intent.createChooser(sendIntent, "Share"))
}

/**
 * Method to navigate setting screen
 */
@Suppress("unused")
fun Activity.openSettingScreen() {
    openSettingScreen(-1)
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}

/**
 * Method to navigate setting screen
 */
fun Activity.openSettingScreen(requestCode: Int) {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    if (requestCode > 0) {
        startActivityForResult(intent, requestCode)
    } else {
        startActivity(intent)
    }
}


/**
 * Method to get installed application time from PackageInfo class.
 * @return returns installed time in timestamp in long format. it returns 0 if any error occurs.
 */
@Suppress("unused")
fun Context.getAppInstalledTime(): Long? {
    try {
        return packageManager?.getPackageInfo(packageName, 0)?.firstInstallTime
    } catch (e: NameNotFoundException) {
        e.printStackTrace()
    }

    return 0
}

/**
 * Method to get last updated application time from PackageInfo class.
 * @return returns last updated time in timestamp in long format. it returns 0 if any error occurs.
 */
@Suppress("unused")
fun Context.getAppUpdatedTime(): Long? {
    try {
        return packageManager?.getPackageInfo(packageName, 0)?.lastUpdateTime
    } catch (e: NameNotFoundException) {
        e.printStackTrace()
    }
    return 0
}

/**
 * Add all intent flag and extra info here.
 *
 * @return returns intent with flag and extra information.
 */
fun Context.createPendingIntent(): Intent {
    val intent = Intent(this, SplashActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    intent.putExtra(
        LAUNCHED_FROM_NOTIFICATION,
        true
    ) // This flag is for app analytics. please do not remove this.
    return intent
}


/**
 * Method to showNotification with Image/Bitmap. if don't want to show bitmap then it will be null initially
 *
 * @param title           notification Title
 * @param message         notification extra message
 * @param intent          pending intent
 * @param channelId       channelID (ANDROID/PUSH)
 * @param NOTIFICATION_ID NOTIFICATION_ID
 */
fun Context.showNotification(
    channelId: String,
    NOTIFICATION_ID: Int,
    title: String,
    message: String,
    intent: Intent
) {

    val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val contentIntent = PendingIntent.getActivity(
        this,
        0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
    )


    val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val color = ContextCompat.getColor(this, R.color.colorPrimary)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            channelId,
            getString(R.string.scheduled_notification),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        // Configure the notification channel.
        notificationChannel.description = getString(R.string.scheduled_notification)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.enableVibration(true)
        notificationChannel.setShowBadge(true)
        mNotificationManager.createNotificationChannel(notificationChannel)
    }
    val mBuilder = NotificationCompat.Builder(this, channelId)
    mBuilder.setSmallIcon(R.mipmap.ic_launcher_round)
    mBuilder.setContentTitle(title).setContentText(message)
    mBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(message))
    mBuilder.setAutoCancel(true)
    mBuilder.setSound(alarmSound)
    mBuilder.setDefaults(Notification.DEFAULT_ALL)
    mBuilder.color = color
    mBuilder.setContentIntent(contentIntent)
    mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build())
}

/**
 * This method will create unique id by using date time and if any exception is occur, are using random class to
 * create unique id for notification.
 */
fun getUniqueNotificationId(): Int {
    val notificationId: Int = try {
        (Date().time / 1000L % Integer.MAX_VALUE).toInt()
    } catch (e: Exception) {
        e.printStackTrace()
        val random = Random()
        random.nextInt(9999 - 1000) + 10
    }
    return notificationId
}

fun Context.getCountryBasedOnSimCardOrNetwork(): String {
    try {
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simCountry = tm.simCountryIso
        if (simCountry != null && simCountry.length == 2) { // SIM country code is available
            return simCountry.uppercase()
        }
        if (tm.phoneType != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
            val networkCountry = tm.networkCountryIso
            return if (networkCountry != null && networkCountry.length == 2) { // network country code is available
                networkCountry.uppercase()
            } else {
                "IN"
            }
        }
    } catch (e: Exception) {
        return "IN"
    }
    return "IN"
}
