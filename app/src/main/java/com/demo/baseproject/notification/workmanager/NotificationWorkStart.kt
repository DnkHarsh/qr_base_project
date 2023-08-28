package com.demo.baseproject.notification.workmanager

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class NotificationWorkStart(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val mPeriodicWorkRequest = PeriodicWorkRequest.Builder(
            NotificationWorkManager::class.java,
            12, TimeUnit.HOURS
        )
            .addTag(NotificationWorkManager::class.java.name).setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                NotificationWorkManager::class.java.name,
                ExistingPeriodicWorkPolicy.KEEP,
                mPeriodicWorkRequest
            )

        return Result.success()
    }
}