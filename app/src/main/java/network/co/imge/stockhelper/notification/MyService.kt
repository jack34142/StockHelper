package network.co.imge.stockhelper.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.ui.activity.MainActivity
import java.text.SimpleDateFormat
import java.util.*

class MyService: Service() {
    private val TAG: String = "MyService"

    companion object {
        private val CHANNEL_ID: String = "5001"

        private var serviceIntent: Intent? = null
        private var builder: Notification.Builder? = null

        fun startService(context: Context){
            if (serviceIntent == null)
                serviceIntent = Intent(context, MyService::class.java)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
        }

        fun stopService(context: Context){
            if (serviceIntent != null){
                context.stopService(serviceIntent)
                builder = null
                serviceIntent = null
            }
        }

        fun updateTime(context: Context){
            if (builder != null){
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                builder!!.setContentText(context.getString(R.string.update_time, SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())))
                notificationManager.notify(CHANNEL_ID.toInt(), builder!!.build());
            }
        }
    }

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

        startForeground(CHANNEL_ID.toInt(), getNotification())
    }

    private fun getNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        builder = Notification.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.running))
            .setContentText(getString(R.string.update_time, SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())))
            .setContentIntent(pendingIntent)

        //设置Notification的ChannelID,否则不能正常显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder!!.setChannelId(CHANNEL_ID)
        }
        return builder!!.build()
    }

    override fun onDestroy() {
        stopForeground(false)
        super.onDestroy()
    }
}