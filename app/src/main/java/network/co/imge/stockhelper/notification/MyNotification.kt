package network.co.imge.stockhelper.notification;

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.ui.activity.MainActivity


class MyNotification private constructor(){
    companion object {
        val CHANNEL_ID: String = "701"
        val instance: MyNotification by lazy { MyNotification() }
    }

    fun makeNotice(context: Context, msg: String){
        wakeupScreen(context)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(context.getString(R.string.notification_notice))
            .setContentText(msg)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true);

        val resultIntent = Intent(context, MainActivity::class.java)

        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addNextIntentWithParentStack(resultIntent)
        // Get the PendingIntent containing the entire back stack
        val resultPendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        builder.setContentIntent(resultPendingIntent)
        NotificationManagerCompat.from(context).notify(CHANNEL_ID.toInt(), builder.build())
    }

    private fun wakeupScreen(context: Context) {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE, "appname::WakeLock"
        )
        wakeLock.acquire(5000)
    }
}
