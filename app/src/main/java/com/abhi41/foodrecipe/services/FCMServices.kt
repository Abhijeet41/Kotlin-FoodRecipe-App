package com.abhi41.foodrecipe.services

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.abhi41.foodrecipe.utils.Constants.Companion.FCM_NOTIFICATION_ID
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FCMServices : FirebaseMessagingService() {
    private val TAG = "FCMServices"

    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        pushNotification(
            message.notification?.title,
            message.notification?.body,
            message.notification?.imageUrl
        )

    }

    private fun pushNotification(title: String?, message: String?, imageUrl: Uri?) {
        notificationManager.notify(
            FCM_NOTIFICATION_ID,
            notificationBuilder.setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(convertUriToBitmap(imageUrl))
                .build()
        )
    }

    private fun convertUriToBitmap(uri: Uri?): Bitmap {
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,Uri.parse(uri.toString()))
        return bitmap
    }

}