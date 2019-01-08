package momonyan.contactlensmanage

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log


class Notifier : BroadcastReceiver() {

    override fun onReceive(content: Context, intent: Intent) {

        //通知がクリックされた時に発行されるIntentの生成
        val sendIntent = Intent(content, TabActivity::class.java)
        val sender = PendingIntent.getActivity(content, 0, sendIntent, 0)

        //通知オブジェクトの生成
        val noti = NotificationCompat.Builder(content)
            .setTicker("交換日です！")
            .setContentTitle("コンタクトマネージャー")
            .setContentText("コンタクトの交換日です")
            .setSmallIcon(R.drawable.contact_app_icon)
            .setVibrate(longArrayOf(0, 200, 100, 200, 100, 200))
            .setAutoCancel(true)
            .setContentIntent(sender)
            .build()
        Log.d("AAA","TESTEMA")

        val manager = content.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, noti)
    }
}