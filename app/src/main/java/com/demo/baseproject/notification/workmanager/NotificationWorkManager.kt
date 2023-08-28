package com.demo.baseproject.notification.workmanager

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.demo.baseproject.R
import com.demo.baseproject.utils.extensions.createPendingIntent
import com.demo.baseproject.utils.extensions.getUniqueNotificationId
import com.demo.baseproject.utils.extensions.showNotification

class NotificationWorkManager(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val channelId = context.packageName + "ANDROID"
        val pendingIntent: Intent = context.createPendingIntent()
        val title = context.resources.getString(R.string.app_name)
        val description = ""
        context.showNotification(
            channelId,
            getUniqueNotificationId(),
            title,
            description,
            pendingIntent
        )
        return Result.success()
    }
}