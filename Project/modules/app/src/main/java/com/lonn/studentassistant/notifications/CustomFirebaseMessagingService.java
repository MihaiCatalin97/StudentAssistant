package com.lonn.studentassistant.notifications;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lonn.studentassistant.R;

import java.util.Random;

public class CustomFirebaseMessagingService extends FirebaseMessagingService {

	@Override
	public void onMessageReceived(RemoteMessage message) {
		super.onMessageReceived(message);

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "STUDENT_ASSISTANT")
				.setContentTitle(message.getNotification().getTitle())
				.setContentText(message.getNotification().getBody())
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.setStyle(new NotificationCompat.BigTextStyle())
				.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				.setSmallIcon(R.mipmap.ic_launcher)
				.setAutoCancel(true);

		NotificationManager notificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(new Random().nextInt(10000), notificationBuilder.build());
	}
}