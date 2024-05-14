package com.example.myapplication.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.myapplication.R
import com.example.myapplication.ui.main.host.MainHostActivity

class NotificationWorker(private val context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        // Obtém dados da entrada do Worker, se necessário
        val notificationId = inputData.getInt("notificationId", 0)
        val title = inputData.getString("title") ?: "Título padrão"
        val text = inputData.getString("text") ?: "Texto padrão"
        val scheduleId = inputData.getLong("scheduleId", 0L)


        // Cria uma Intent para abrir a tela específica
        val intent = Intent(context, MainHostActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("scheduleId", scheduleId)
        }


        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                applicationContext, notificationId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                applicationContext, notificationId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        createNotificationChannel()

        // Cria a notificação
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // Mostra a notificação
        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, notification)
        }

        return Result.success()
    }

    private fun createNotificationChannel() {
        val name = "Habit Tracker Channel"
        val descriptionText = "Channel for habit tracker notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
    companion object {
        const val CHANNEL_ID = "habit_tracker_channel"
    }
}