package network.co.imge.stockhelper.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.ui.activity.MainActivity

class MyService: Service() {
    private val TAG: String = "MyService"

    private val CHANNEL_ID: String = "5001"

    override fun onBind(p0: Intent?): IBinder? {
        return null;
    }

    override fun onCreate() {
        super.onCreate()
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //创建NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_running),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        startForeground(1, getNotification())
    }

    private fun getNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = Notification.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.running))
            .setContentIntent(pendingIntent)

        //设置Notification的ChannelID,否则不能正常显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID)
        }
        return builder.build()
    }

    override fun onDestroy() {
        stopForeground(false)
        super.onDestroy()
    }
}