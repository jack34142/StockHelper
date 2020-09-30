package network.co.imge.stockhelper.app

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.notification.MyNotification

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initNotification()
    }

    private fun initNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.notification_notice)
            //            String description = getString(R.string.channel_description);
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val noticeChannel =
                NotificationChannel(MyNotification.CHANNEL_ID, name, importance)
            noticeChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            //            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(noticeChannel)
        }
    }
}