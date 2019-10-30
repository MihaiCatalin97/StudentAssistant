package com.lonn.studentassistant.notifications.abstractions;

import android.app.Notification;
import android.content.Context;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.entities.BaseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class NotificationCreator<T extends BaseEntity>
{
    protected int smallIconId;
    protected Map<String, Integer> notificationMap = new HashMap<>();
    protected NotificationManagerCompat notificationManager;

    public void showNotification(Context context, DatabaseResponse<T> response)
    {
        Notification notification = new NotificationCompat.Builder(context, "1")
                .setSmallIcon(smallIconId)
                .setContentTitle(getTitle(response))
                .setContentText(getText(response))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        if (notificationManager == null)
            notificationManager = NotificationManagerCompat.from(context);

        UUID id = UUID.randomUUID();
        notificationMap.put(response.getAction(), id.hashCode());
        notificationManager.notify(id.hashCode(), notification);
    }

    protected abstract String getTitle(DatabaseResponse<T> response);

    protected abstract String getText(DatabaseResponse<T> response);
}

