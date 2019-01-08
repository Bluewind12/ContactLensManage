package momonyan.contactlensmanage

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import java.util.*

class AlarmPush : AppCompatActivity() {
    fun setNotification(request: Int, sharedPreferences: SharedPreferences, act: FragmentActivity?) {
        val year: Int
        val month: Int
        val day: Int
        val requestCode: Int

        val notificationHour = sharedPreferences.getInt("pushHour", 0)
        val notificationMinute = sharedPreferences.getInt("pushMinute", 0)

        when (request) {
            0 -> {
                year = sharedPreferences.getInt("Year", 0)
                month = sharedPreferences.getInt("Month", 0)
                day = sharedPreferences.getInt("Day", 0)
                requestCode = 0
            }
            1 -> {
                year = sharedPreferences.getInt("Year2", 0)
                month = sharedPreferences.getInt("Month2", 0)
                day = sharedPreferences.getInt("Day2", 0)
                requestCode = 1
            }
            else -> {
                year = sharedPreferences.getInt("Year", 0)
                month = sharedPreferences.getInt("Month", 0)
                day = sharedPreferences.getInt("Day", 0)
                requestCode = 2
            }
        }

        val triggerTime = Calendar.getInstance()
        triggerTime.set(year, month - 1, day, notificationHour, notificationMinute, requestCode)

        val nowTime = Calendar.getInstance()
        val diff = triggerTime.timeInMillis - nowTime.timeInMillis
        if (diff > 0) {
            val intent = Intent(act, Notifier::class.java)
            intent.putExtra("LR", requestCode)
            val sender = PendingIntent.getBroadcast(act, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            val manager = act!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
            manager!!.set(AlarmManager.RTC_WAKEUP, triggerTime.timeInMillis, sender)
        }
    }

    fun setNotification(request: Int, sharedPreferences: SharedPreferences, act: Context) {
        val year: Int
        val month: Int
        val day: Int
        val requestCode: Int

        val notificationHour = sharedPreferences.getInt("pushHour", 0)
        val notificationMinute = sharedPreferences.getInt("pushMinute", 0)

        when (request) {
            0 -> {
                year = sharedPreferences.getInt("Year", 0)
                month = sharedPreferences.getInt("Month", 0)
                day = sharedPreferences.getInt("Day", 0)
                requestCode = 0
            }
            1 -> {
                year = sharedPreferences.getInt("Year2", 0)
                month = sharedPreferences.getInt("Month2", 0)
                day = sharedPreferences.getInt("Day2", 0)
                requestCode = 1
            }
            else -> {
                year = sharedPreferences.getInt("Year", 0)
                month = sharedPreferences.getInt("Month", 0)
                day = sharedPreferences.getInt("Day", 0)
                requestCode = 2
            }
        }
        val triggerTime = Calendar.getInstance()
        triggerTime.set(year, month - 1, day, notificationHour, notificationMinute, requestCode)

        val nowTime = Calendar.getInstance()
        val diff = triggerTime.timeInMillis - nowTime.timeInMillis
        if (diff > 0) {
            val intent = Intent(act, Notifier::class.java)
            intent.putExtra("LR", requestCode)
            val sender = PendingIntent.getBroadcast(act, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            val manager = act.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
            manager!!.set(AlarmManager.RTC_WAKEUP, triggerTime.timeInMillis, sender)
        }
    }
}