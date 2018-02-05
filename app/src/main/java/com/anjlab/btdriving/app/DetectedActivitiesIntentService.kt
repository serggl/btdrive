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
                    resources.getStringArray(R.array.activity_kinds)[activity.type],
                    "${getString(R.string.activity_confidence)}: ${activity.confidence}",
                    3
                )
            }
            if (activity.confidence < 50) return

            val onTheWheels = activity.type == DetectedActivity.IN_VEHICLE || activity.type == DetectedActivity.ON_BICYCLE
            if (onTheWheels && !pref.driving) {
                pref.driving = true
                pref.stillCounter = 0
                Bluetooth().enable()
            }
            else if (pref.driving) {
                var disable = false
                when (activity.type) {
                    DetectedActivity.ON_FOOT, DetectedActivity.RUNNING, DetectedActivity.WALKING -> {
                        // walk out of a car and move
                        disable = true
                    }
                    DetectedActivity.STILL -> {
                        if (activity.confidence > 90) {
                            // ended driving, put phone on the table at least for 3 min
                            val counter = pref.stillCounter
                            if (counter >= 10) {
                                disable = true
                            } else {
                                pref.stillCounter = counter + 1
                            }
                        }
                    }
                    else -> pref.stillCounter = 0
                }
                if (disable) {
                    pref.driving = false
                    pref.stillCounter = 0
                    Bluetooth().disable()
                }
            }
        }
    }

    companion object {
        private const val TAG = "DetectedActivitiesIntentService"
    }
}
