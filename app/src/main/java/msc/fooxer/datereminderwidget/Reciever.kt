package msc.fooxer.datereminderwidget

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat.getSystemService
import android.util.Log

var NOTIFICATION = "not"

class Reciever : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Name"
            val descriptionText = "Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val id = intent.getIntExtra("id",0)
            //val not = intent.getParcelableExtra<Notification>(NOTIFICATION)
            notificationManager.createNotificationChannel(channel)
            Log.e("RECIEVER", "Reciever ok")
            notificationManager.notify(id,createNotification(context))

        }

    }
    fun createNotification(context:Context): Notification {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        /*val intent = Intent(context,Reciever::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,0,intent,0)*/
        val builder =  NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.abc_btn_switch_to_on_mtrl_00001)
            .setContentTitle("Напоминание")
            .setContentText("Ваша дата ${dateFormat.format(newDate)} наступила!")
            //.setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        Log.e("NOTIFICATION", "Notification created")
        return builder.build()
    }

}