package com.demo.baseproject.utils

import android.app.Activity
import android.content.Context
import com.demo.baseproject.storage.AppPref
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory

class GoogleInAppReview(context: Context) {

    private var manager: ReviewManager? = null

    init {
        manager = ReviewManagerFactory.create(context)
    }

    fun show(activity: Activity) {
        val request = manager?.requestReviewFlow()
        request?.addOnCompleteListener {
            if (request.isSuccessful) {
                val reviewInfo = request.result
                manager?.launchReviewFlow(activity, reviewInfo)
                var currentCount =
                    AppPref.getInstance().getValue(AppPref.SHOW_IN_APP_REVIEW_AFTER_SESSIONS, 0)
                currentCount += 10
                AppPref.getInstance()
                    .setValue(AppPref.SHOW_IN_APP_REVIEW_AFTER_SESSIONS, currentCount)
            }
        }
    }

    companion object {
        var instance: GoogleInAppReview? = null

        fun init(context: Context): GoogleInAppReview {
            if (instance == null) {
                instance = GoogleInAppReview(context)
            }
            return instance!!
        }
    }

}