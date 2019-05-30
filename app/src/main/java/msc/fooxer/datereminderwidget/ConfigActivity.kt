package msc.fooxer.datereminderwidget

import android.app.*
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_config.*
import kotlinx.android.synthetic.main.widget.dateTextView
import kotlinx.android.synthetic.main.widget.setDateButton
import java.text.SimpleDateFormat
import java.util.*
var myDate = Date()
var newDate = Date(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH+1)
var daysDiff: Long = -1
val WIDGET_PREF = "widget_pref"
val dateFormat = SimpleDateFormat("dd MMM yyy", Locale.getDefault())
class ConfigActivity : AppCompatActivity() {

   //companion object val WIDGET_PREF = "widget_pref"
    var widgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    var resultValue = Intent()
    lateinit var newDateTv : TextView
    lateinit var diffTv : TextView

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_config)
            val intent = intent
            val extras = intent.extras
            if (extras!=null) {
                widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
            }
            if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
                finish()
            resultValue = Intent()
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            setResult(Activity.RESULT_CANCELED, resultValue)
            dateTextView.text = "Cегодня: ${dateFormat.format(Date())}"

            newDateTv = findViewById(R.id.newDateTextView)
            diffTv = findViewById(R.id.daystextView)
            loadDate()
            setDateButton.setOnClickListener{
                val newFragment = DatePickerFragment()
                newFragment.show(supportFragmentManager, "datePicker")

            }
            saveButton.setOnClickListener {
                saveDate()
                setResult(Activity.RESULT_OK, resultValue)
                createNotification(this,1)
                finish()

            }


    }
    override fun onResume() {
        super.onResume()
        newDateTv.text = "Ваша дата: ${dateFormat.format(newDate)}"
        diffTv.text = "$daysDiff"

    }
    fun saveDate () {
        val sPref = getSharedPreferences(WIDGET_PREF,Context.MODE_PRIVATE)
        val editor = sPref.edit()
        editor.putLong("SAVED_DATE$widgetId",newDate.time)
        editor.apply()
        val appWidgetManager = AppWidgetManager.getInstance(this)
        updateWidget(this, appWidgetManager, sPref, widgetId)
    }
    fun loadDate() {
        val sPref = getSharedPreferences(WIDGET_PREF,Context.MODE_PRIVATE)
        val tomorrow = Calendar.getInstance()
        tomorrow.add(Calendar.DATE,1)
        val dateLong = sPref.getLong("SAVED_DATE$widgetId", Date(tomorrow.time.time).time)
        newDate.time = dateLong
        val difference: Long = newDate.time - myDate.time
        daysDiff = (difference / (1000*60*60*24))

    }
    private fun createNotification(context:Context, notificationId: Int) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Name"
            val descriptionText = "Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            notificationManager.createNotificationChannel(channel)
           val builder =  NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_tile_bg)
                .setContentTitle("Title")
                .setContentText("O God help")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setWhen(2000)
            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(notificationId, builder.build())
            }
        }
        }

    }

