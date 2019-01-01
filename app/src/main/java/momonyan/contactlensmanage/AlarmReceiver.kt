package momonyan.contactlensmanage

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val id = intent.getIntExtra(NOTIFICATION_ID, 0)
        val content = intent.getStringExtra(NOTIFICATION_CONTENT)
        notificationManager.notify(id, buildNotification(context, content))
    }

    private fun buildNotification(context: Context, content: String): Notification {
        val builder = Notification.Builder(context)
        builder.setContentTitle("Notification!!")
            .setContentText(content)
            .setSmallIcon(android.R.drawable.sym_def_app_icon)

        return builder.build()
    }

    companion object {
        var NOTIFICATION_ID = "notificationId"
        var NOTIFICATION_CONTENT = "content"
    }
}