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

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import android.provider.Settings
import android.support.v7.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    private val fragment = GeneralPreferenceFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager.beginTransaction().replace(
            android.R.id.content,
            fragment
        ).commit()
    }

    override fun onResume() {
        super.onResume()
        (fragment.findPreference(KEY_DOZE_MODE) as SwitchPreference).isChecked =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).isIgnoringBatteryOptimizations(packageName)
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    class GeneralPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_general)
            findPreference(KEY_DOZE_MODE).onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _, _ ->
                    requestIgnoreBatteryOptimizations()
                    false
                }

            findPreference(KEY_ENABLED).onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _, value ->
                    val recognition = Recognition(activity)
                    if (value as Boolean) {
                        Bluetooth().disable()
                        recognition.start()
                    } else {
                        recognition.stop()
                    }
                    true
                }
        }

        private fun requestIgnoreBatteryOptimizations() {
            // TODO: ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS will be blocked by Google
            val intent = Intent()
            val packageName = context.packageName
            val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            if (pm.isIgnoringBatteryOptimizations(packageName))
                intent.action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
            else {
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:" + packageName)
            }
            context.startActivity(intent)
        }
    }

    companion object {
        const val KEY_ENABLED = "enabled"
        const val KEY_DOZE_MODE = "doze_mode"
    }
}
