package com.thss.lunchtime.component

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.thss.lunchtime.R
import android.content.Context

const val CHANNLE_ID = "lunchTime"

fun showNotification(context: Context) {
    var builder = NotificationCompat.Builder(context, CHANNLE_ID)
        .setSmallIcon(R.drawable.forumicon)
        .setContentTitle("新消息通知")
        .setContentText("XXX点赞了你的帖子")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notify(1, builder.build())
    }
}

// 为了兼容Android 8.0及更高版本，传递通知之前，必须在系统中注册应用程序的通知通道。创建好后在 onCreate 函数内调用
private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
        val channel = NotificationChannel(CHANNLE_ID, "lunchTime", NotificationManager.IMPORTANCE_HIGH).apply { description = "LUNCHTIME" }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}