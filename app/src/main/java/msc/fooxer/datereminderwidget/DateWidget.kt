package msc.fooxer.datereminderwidget

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.provider.Settings.Global.getString
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v4.content.ContextCompat.getSystemServiceName
import android.support.v4.content.res.TypedArrayUtils.getText
import android.widget.RemoteViews
import java.util.*
val CHANNEL_ID = "CHANNEL_ID"

class DateWidget : AppWidgetProvider() {


    override fun onEnabled(context: Context?) {
        super.onEnabled(context)


    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        val sp : SharedPreferences = context.getSharedPreferences(WIDGET_PREF,Context.MODE_PRIVATE)
        if (appWidgetIds != null) {
            for ( id in appWidgetIds){
                updateWidget(context, appWidgetManager, sp, id)
            }
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        val editor = context.getSharedPreferences(WIDGET_PREF,Context.MODE_PRIVATE).edit()
        if (appWidgetIds != null) {
            for (widgetID in appWidgetIds){
                editor.remove("SAVED_DATE$widgetID")
            }
            editor.apply()
        }
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
    }

    }



fun updateWidget(context: Context?, appWidgetManager: AppWidgetManager?, sp: SharedPreferences, widgetId: Int) {
    val dateLong = sp.getLong("SAVED_DATE$widgetId", Date(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH+1).time)
    newDate.time = dateLong
    val difference: Long = newDate.time - myDate.time
    daysDiff = (difference / (1000 * 60 * 60 * 24))
    val widgetView = RemoteViews(context?.packageName, R.layout.widget)
    widgetView.setTextViewText(R.id.newDateTextView, "Ваша дата: ${dateFormat.format(newDate)}")
    widgetView.setTextViewText(R.id.daystextView, daysDiff.toString())
    widgetView.setTextViewText(R.id.dateTextView, "Сегодня: ${dateFormat.format(Date())}")
    widgetView.setOnClickPendingIntent(R.id.setDateButton, callActivity(context,widgetId))
    if (appWidgetManager != null) {
        appWidgetManager.updateAppWidget(widgetId, widgetView)
    }

}
fun callActivity(context:Context?, widgetID: Int) : PendingIntent{
    val configIntent = Intent(context, ConfigActivity::class.java)
    configIntent.action = AppWidgetManager.ACTION_APPWIDGET_CONFIGURE
    configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID)
    val pIntent = PendingIntent.getActivity(
        context, widgetID,
        configIntent, 0
    )
    return pIntent
}

