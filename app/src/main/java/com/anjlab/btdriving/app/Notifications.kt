package com.anjlab.btdriving.app

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat

class Notifications {
    companion object {
        private const val NOTIFY_CHANNEL = "bt_auto"

        private fun getNotificationManager(context: Context) : NotificationManager {
            return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        @TargetApi(Build.VERSION_CODES.O)
        private fun createNotificationChannel(context: Context) {
            val followersChannel = NotificationChannel(
                NOTIFY_CHANNEL,
                context.getString(R.string.notification_channel),
                NotificationManager.IMPORTANCE_DEFAULT)
            getNotificationManager(context).createNotificationChannel(followersChannel)
        }

        @Suppress("DEPRECATION")
        fun createNotification(context: Context, title: String, message: String, id: Int) {
            val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(context)
                NotificationCompat.Builder(context, NOTIFY_CHANNEL)
            } else {
                NotificationCompat.Builder(context)
            }

            with(builder) {
                setSmallIcon(R.drawable.ic_notification)
                setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_round))
                setContentTitle(title)
                setContentText(message)
                setAutoCancel(true)

                setContentIntent(PendingIntent.getActivity(context,
                    0,
                    Intent(context, SettingsActivity::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT))

                getNotificationManager(context).notify(id, build())
            }
        }
    }
}