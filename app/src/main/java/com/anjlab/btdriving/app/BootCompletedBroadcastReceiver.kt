package com.anjlab.btdriving.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootCompletedBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != null && intent.action == Intent.ACTION_BOOT_COMPLETED) {
            if (Preferences(context).enabled) {
                Recognition(context).start()
            }
        }
    }
}
