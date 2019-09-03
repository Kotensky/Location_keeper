package com.gemicle.locationkeeper.utils


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gemicle.locationkeeper.R
import com.gemicle.locationkeeper.ui.main.MainActivity

class NotificationHelper(private val context: Context) {

    private var notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {

        const val NOTIFICATION_ID = 12345678
        private const val CHANNEL_ID = "location_keeper_channel"

    }

    init {
        createNotificationChannel()
    }

    fun notifyLocation(lastLocation: String){
        notificationManager.notify(NOTIFICATION_ID, buildNotification(lastLocation))
    }

    fun buildNotification(lastLocation: String): Notification {

        val activityPendingIntent = PendingIntent.getActivity(
            context, 0,
            Intent(context, MainActivity::class.java), 0
        )

        val builder = NotificationCompat.Builder(context,
            CHANNEL_ID
        )
            .addAction(
                R.mipmap.ic_launcher, context.getString(R.string.launch_activity),
                activityPendingIntent
            )
            .setContentText(lastLocation)
            .setContentTitle(Utils.getLocationTitle(context))
            .setOngoing(true)
            .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setTicker(lastLocation)
            .setWhen(System.currentTimeMillis())

        return builder.build()
    }

    private fun createNotificationChannel() {
        notificationManager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )
    }
}