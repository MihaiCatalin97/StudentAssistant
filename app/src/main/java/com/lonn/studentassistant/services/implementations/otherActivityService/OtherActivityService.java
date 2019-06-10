package com.lonn.studentassistant.services.implementations.otherActivityService;

import android.content.Intent;

import com.lonn.studentassistant.entities.OtherActivity;
import com.lonn.studentassistant.notifications.implementations.OtherActivityNotificationCreator;
import com.lonn.studentassistant.services.abstractions.DatabaseService;
import com.lonn.studentassistant.services.implementations.otherActivityService.dataAccessLayer.OtherActivityRepository;

public class OtherActivityService extends DatabaseService<OtherActivity>
{
    public OtherActivityRepository instantiateRepository()
    {
        return OtherActivityRepository.getInstance(this);
    }

    public OtherActivityNotificationCreator instantiateCreator()
    {
        return new OtherActivityNotificationCreator();
    }

    public void onConnected()
    {
    }

    public void handleDestroy()
    {
        sendBroadcast(new Intent("RestartCourseService"));
    }
}
