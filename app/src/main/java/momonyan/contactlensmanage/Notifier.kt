package momonyan.contactlensmanage

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat


class Notifier : BroadcastReceiver() {

    var vibrate = longArrayOf(0, 200, 100, 200, 100, 200)

    override fun onReceive(content: Context, intent: Intent) {

        val id = intent.getIntExtra("LR", 0)
        //通知がクリックされた時に発行されるIntentの生成
        val sendIntent = Intent(content, TabActivity::class.java)
        val sender = PendingIntent.getActivity(content, 0, sendIntent, 0)

        val message: String

        if (id == 0) {
            message = "コンタクト(L)の交換日です！"
        } else if (id == 1) {
            message = "コンタクト(R)の交換日です！"
        } else {
            message = "コンタクトの交換日です！"
        }
        //通知オブジェクトの生成
        val noti = NotificationCompat.Builder(content)
            .setTicker("交換日です！")
            .setContentTitle("コンタクトマネージャー")
            .setContentText(message)
            .setSmallIcon(R.drawable.contact_app_icon)
            .setVibrate(vibrate)
            .setAutoCancel(true)
            .setContentIntent(sender)
            .build()

        val manager = content.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify("Notify", id, noti)
    }

    //バイブレーション機能の使用について
    fun setVibrateMode(setting: Boolean) {
        if (setting) {
            vibrate = longArrayOf(0, 200, 100, 200, 100, 200)
        } else {
            vibrate = longArrayOf(0, 0, 0, 0, 0, 0)
        }
    }
}