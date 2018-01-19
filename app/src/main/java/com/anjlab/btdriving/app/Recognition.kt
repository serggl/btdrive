/**
 * Copyright 2018 AnjLab
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anjlab.btdriving.app

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.ActivityRecognitionClient

class Recognition(private var context: Context) {
    private val client = ActivityRecognitionClient(context)
    private val pref = Preferences(context)

    fun start() {
        pref.driving = false
        val task = client.requestActivityUpdates(
            DETECTION_INTERVAL_IN_MILLISECONDS,
            getActivityDetectionPendingIntent()
        )

        task.addOnSuccessListener({
            notify( R.string.notification_title_enabled, R.string.notification_msg_enabled)
            pref.enabled = true
        })

        task.addOnFailureListener({
            notify( R.string.notification_title_error, R.string.notification_msg_enable_failed)
            pref.enabled = false
        })
    }

    fun stop() {
        pref.driving = false
        val task = client.removeActivityUpdates(getActivityDetectionPendingIntent())
        task.addOnSuccessListener({
            notify( R.string.notification_title_disabled, R.string.notification_msg_disabled)
            pref.enabled = false
        })

        task.addOnFailureListener({
            notify( R.string.notification_title_error, R.string.notification_msg_disable_failed)
            pref.enabled = true
        })
    }

    private fun notify(title: Int, message: Int) {
        Notifications.createNotification(context, context.getString(title), context.getString(message), 1)
    }

    private fun getActivityDetectionPendingIntent(): PendingIntent {
        val intent = Intent(context, DetectedActivitiesIntentService::class.java)
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    companion object {
        private const val DETECTION_INTERVAL_IN_MILLISECONDS = (30 * 1000).toLong() // 30 seconds
    }
}