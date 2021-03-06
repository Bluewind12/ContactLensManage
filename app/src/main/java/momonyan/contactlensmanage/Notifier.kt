package momonyan.contactlensmanage

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat


class Notifier : BroadcastReceiver() {

    var vibrate = longArrayOf(0, 200, 100, 200, 100, 200)

    override fun onReceive(content: Context, intent: Intent) {

        val id = intent.getIntExtra("LR", 0)
        //通知がクリックされた時に発行されるIntentの生成
        val sendIntent = Intent(content, TabActivity::class.java)
        val sender = PendingIntent.getActivity(content, 0, sendIntent, 0)

        val preference = content.getSharedPreferences("Data", Context.MODE_PRIVATE)
        val vibrateMode = preference.getBoolean("Vibrate", true)

        when (vibrateMode) {
            true -> vibrate = longArrayOf(0, 200, 100, 200, 100, 200)
            else -> vibrate = longArrayOf(0, 0, 0, 0, 0, 0)
        }

        val message: String
        val notiferIcon: Int

        when (id) {
            0 -> {
                message = "コンタクト(L)の交換日です！"
                notiferIcon = R.drawable.l_icon
            }
            1 -> {
                message = "コンタクト(R)の交換日です！"
                notiferIcon = R.drawable.r_icon
            }
            else -> {
                message = "コンタクトの交換日です！"
                notiferIcon = R.drawable.lr_icon
            }
        }

        //通知オブジェクトの生成
        val noti = NotificationCompat.Builder(content)
            .setTicker("交換日です！")
            .setContentTitle("コンタクトマネージャー")
            .setContentText(message)
            .setSmallIcon(notiferIcon)
            .setVibrate(vibrate)
            .setAutoCancel(true)
            .setContentIntent(sender)
            .build()

        val manager = content.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify("Notify", id, noti)
    }
}