package com.anjlab.btdriving.app

import android.app.IntentService
import android.content.Intent
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity

class DetectedActivitiesIntentService : IntentService(TAG) {
    override fun onHandleIntent(intent: Intent?) {
        val result = ActivityRecognitionResult.extractResult(intent)

        val activity = result.probableActivities.maxBy { it.confidence }
        if (activity != null) {
            val pref = Preferences(this)

            if (pref.debug) {
                Notifications.createNotification(
                    this,
                    getActivityString(activity.type),
                    "confidence: ${activity.confidence}",
                    3
                )
            }

            if (activity.confidence >= 70) {
                if (activity.type == DetectedActivity.IN_VEHICLE) {
                    if (!pref.driving) {
                        pref.driving = true
                        Bluetooth().enable()
                    }
                }
                else if (pref.driving && activity.confidence > 50 && (
                        activity.type == DetectedActivity.ON_FOOT ||
                            activity.type == DetectedActivity.RUNNING ||
                            activity.type == DetectedActivity.WALKING)) {
                    pref.driving = false
                    Bluetooth().disable()
                }
            }
        }
    }

    companion object {
        private const val TAG = "DetectedActivitiesIntentService"

        internal fun getActivityString(detectedActivityType: Int): String {
            return when (detectedActivityType) {
                DetectedActivity.IN_VEHICLE -> "in_vehicle"
                DetectedActivity.ON_BICYCLE -> "on_bicycle"
                DetectedActivity.ON_FOOT -> "on_foot"
                DetectedActivity.RUNNING -> "running"
                DetectedActivity.STILL -> "still"
                DetectedActivity.TILTING -> "tilting"
                DetectedActivity.UNKNOWN -> "unknown"
                DetectedActivity.WALKING -> "walking"
                else -> "unidentifiable_activity"
            }
        }
    }
}
