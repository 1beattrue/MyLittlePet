package edu.mirea.onebeattrue.mylittlepet.data.pets

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import edu.mirea.onebeattrue.mylittlepet.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val notificationManager = getSystemService(
                it,
                NotificationManager::class.java
            ) as NotificationManager

            createNotificationChannel(notificationManager)

            val title = intent?.getStringExtra(EXTRA_TITLE_KEY)
                ?: it.resources.getString(R.string.notification_title)
            val text = intent?.getStringExtra(EXTRA_TEXT_KEY)
                ?: it.resources.getString(R.string.notification_text)

            val notification = NotificationCompat.Builder(it, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build()

            val notificationId = System.currentTimeMillis().toInt()
            notificationManager.notify(notificationId, notification)
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(notificationChannel)
    }

    companion object {
        private const val CHANNEL_ID = "channel_69"
        private const val CHANNEL_NAME = "channel_pet_events"

        private const val EXTRA_TITLE_KEY = "title_key"
        private const val EXTRA_TEXT_KEY = "text_key"

        fun newIntent(context: Context, title: String, text: String): Intent {
            return Intent(context, AlarmReceiver::class.java).apply {
                putExtra(EXTRA_TITLE_KEY, title)
                putExtra(EXTRA_TEXT_KEY, text)
            }
        }
    }
}