package com.example.todoapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val todoTitle = intent.getStringExtra("title") ?: "ToDo"
        val todoId = intent.getStringExtra("todo_id") ?: ""

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "todo_channel"
            val channelName = "ToDo Reminders"
            val channelDescription = "Channel for ToDo Reminders"

            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                description = channelDescription
            }

            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = todoId.hashCode()

        val notificationIntent = Intent(context, InnerListActivity::class.java).apply {
            putExtra("todo_id", todoId)
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, "todo_channel")
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("Reminder: $todoTitle")
            .setContentText("Don't forget to complete your ToDo!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}
