package com.example.notificationservice

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var notificationManager:NotificationManagerCompat
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        notificationManager = NotificationManagerCompat.from(this)

        binding.apply {
            btnShow.setOnClickListener {
                notificationManager.notify(1,notifyBuilder())
            }
            btnHide.setOnClickListener {
                notificationManager.cancel(1)
            }
        }
    }

    private fun notifyBuilder(): Notification {
        val notificationItem = RemoteViews(packageName, R.layout.notification_item)
        val notificationItemBig = RemoteViews(packageName, R.layout.notification_item_big)

        val clickIntent = Intent(this, MyService::class.java)

        val clickPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getService(this, 0, clickIntent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getService(this, 0, clickIntent, PendingIntent.FLAG_NO_CREATE)
        }

        notificationItem.setTextViewText(R.id.tv_title_1, "Kichik sarlavha")
        notificationItem.setTextViewText(R.id.tv_about_1, "Kichik matn")

        notificationItemBig.setImageViewResource(R.id.image_view_expanded,
            R.drawable.ic_launcher_background)
        notificationItemBig.setOnClickPendingIntent(R.id.image_view_expanded, clickPendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("exampleChannel", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this@MainActivity, "exampleChannel")
            .setSmallIcon(R.drawable.ic_launcher_foreground).setCustomContentView(notificationItem)
            .setCustomBigContentView(notificationItemBig)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle()).build()
    }
    }
