package com.demo.baseproject.ui.activities

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.applovin.sdk.AppLovinSdk
import com.demo.baseproject.BuildConfig
import com.demo.baseproject.R
import com.demo.baseproject.ads.listeners.AdStatusListener
import com.demo.baseproject.databinding.ActivityMainBinding
import com.demo.baseproject.events.EventLogger
import com.demo.baseproject.network.NetworkConstants
import com.demo.baseproject.notification.workmanager.NotificationWorkStart
import com.demo.baseproject.storage.AppPref
import com.demo.baseproject.ui.base.BaseActivity
import com.demo.baseproject.utils.GoogleInAppReview
import com.demo.baseproject.utils.extensions.getDelayFromNowTime
import com.demo.baseproject.utils.extensions.showMandatoryUpdateDialog
import com.demo.baseproject.utils.extensions.showSnackBar
import com.google.android.gms.ads.MobileAds
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.inmobi.sdk.InMobiSdk
import com.inmobi.sdk.SdkInitializationListener
import com.ironsource.mediationsdk.IronSource
import com.unity3d.ads.IUnityAdsInitializationListener
import com.unity3d.ads.UnityAds
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate),
    AdStatusListener {

    @Inject
    lateinit var firebaseRemoteConfig: FirebaseRemoteConfig

    @Inject
    lateinit var eventLogger: EventLogger

    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var consentInformation: ConsentInformation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    /**
     * Class's initialize method which is called from onCreate method of class.
     */
    private fun init() {
        initAds()
        workManagerStart()
        checkForInAppUpdate()
        showConsentFormIfRequired()

        // check for mandatory update
        if (firebaseRemoteConfig.getLong(NetworkConstants.MIN_VERSION_CODE) > BuildConfig.VERSION_CODE) {
            showMandatoryUpdateDialog()
        }
    }

    private fun initAds() {
        MobileAds.initialize(this)

        IronSource.init(
            this,
            getString(R.string.IRONSOURCE_APP_ID),
            {},
            IronSource.AD_UNIT.REWARDED_VIDEO,
            IronSource.AD_UNIT.INTERSTITIAL,
            IronSource.AD_UNIT.BANNER
        )

        UnityAds.initialize(
            applicationContext,
            getString(R.string.UNITY_APP_ID),
            false,
            object : IUnityAdsInitializationListener {
                override fun onInitializationComplete() {
                }

                override fun onInitializationFailed(
                    error: UnityAds.UnityAdsInitializationError?,
                    message: String?
                ) {
                }
            })

        InMobiSdk.init(
            this,
            getString(R.string.INMOBI_APP_ID),
            JSONObject(),
            object : SdkInitializationListener {
                override fun onInitializationComplete(error: Error?) {
                }
            })

        AppLovinSdk.getInstance(this).mediationProvider = "max"
        AppLovinSdk.initializeSdk(this) {
        }
    }

    /**
     * workManagerStart is use for start work manager for daily notification
     */
    private fun workManagerStart() {
        val timeDifference: Long = getDelayFromNowTime()
        val oneTimeWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequest.Builder(NotificationWorkStart::class.java)
                .setInitialDelay(timeDifference, TimeUnit.MINUTES).build()
        WorkManager.getInstance(applicationContext).enqueue(oneTimeWorkRequest)
    }

    override fun onResume() {
        super.onResume()
        showInAppReview()
    }

    private fun checkForInAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateManager.registerListener(installStateUpdatedListener)
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                startInAppUpdate(appUpdateInfo)
            }
        }
    }

    private fun startInAppUpdate(appUpdateInfo: AppUpdateInfo) {
        var appUpdateType = -1
        if (appUpdateInfo.updatePriority() <= 3) {
            // Flexible update
            appUpdateType = AppUpdateType.FLEXIBLE
        } else if (appUpdateInfo.updatePriority() >= 4) {
            // Immediate update
            appUpdateType = AppUpdateType.IMMEDIATE
        }
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo, inAppUpdateLauncher, AppUpdateOptions.newBuilder(appUpdateType).build()
        )
    }

    private fun unregisterAppUpdateListener() {
        appUpdateManager.unregisterListener(installStateUpdatedListener)
    }

    private val inAppUpdateLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode != RESULT_OK) {
                unregisterAppUpdateListener()
            }
        }

    private val installStateUpdatedListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.INSTALLED || state.installStatus() == InstallStatus.CANCELED) {
            unregisterAppUpdateListener()
        } else if (state.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
            binding.root.showSnackBar(getString(R.string.update_downloaded_message)) {
                appUpdateManager.completeUpdate()
                unregisterAppUpdateListener()
            }
        }
    }

    private fun showInAppReview() {
        val sessionCount = AppPref.getInstance().getValue(AppPref.SESSION_COUNT, 0)
        val showReviewAfter =
            AppPref.getInstance().getValue(AppPref.SHOW_IN_APP_REVIEW_AFTER_SESSIONS, 0)
        if (sessionCount >= showReviewAfter) {
            val inAppReview = GoogleInAppReview.init(this)
            inAppReview.show(this)
        }
    }

    private fun showConsentFormIfRequired() {
        val params = ConsentRequestParameters.Builder().setTagForUnderAgeOfConsent(false).build()
        consentInformation = UserMessagingPlatform.getConsentInformation(this)
        consentInformation.requestConsentInfoUpdate(this, params, {
            if (consentInformation.isConsentFormAvailable) {
                loadConsentForm()
            }
        }, {})
    }

    private fun loadConsentForm() {
        UserMessagingPlatform.loadConsentForm(this, { consentForm ->
            if (consentInformation.consentStatus == ConsentInformation.ConsentStatus.REQUIRED) {
                consentForm.show(this) {
                    loadConsentForm()
                }
            }
        }) {}
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterAppUpdateListener()
    }
}