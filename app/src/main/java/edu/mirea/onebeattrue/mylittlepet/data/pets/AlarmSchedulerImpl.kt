package edu.mirea.onebeattrue.mylittlepet.data.pets

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.AlarmItem
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.AlarmScheduler
import javax.inject.Inject

class AlarmSchedulerImpl @Inject constructor(
    private val context: Application
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(item: AlarmItem) {
        val intent = AlarmReceiver.newIntent(context, item.title, item.text)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (!item.repeatable) {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                item.time,
                pendingIntent
            )
        } else {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                item.time,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }

    override fun cancel(item: AlarmItem) {

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.hashCode(),
            Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
    }

}