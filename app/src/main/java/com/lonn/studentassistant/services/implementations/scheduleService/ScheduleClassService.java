package com.lonn.studentassistant.services.implementations.scheduleService;

import android.content.Intent;

import com.lonn.studentassistant.entities.ScheduleClass;
import com.lonn.studentassistant.notifications.implementations.ScheduleNotificationCreator;
import com.lonn.studentassistant.services.abstractions.DatabaseService;
import com.lonn.studentassistant.services.implementations.scheduleService.dataAccessLayer.ScheduleRepository;

public class ScheduleClassService extends DatabaseService<ScheduleClass>
{
    public ScheduleRepository instantiateRepository()
    {
        return ScheduleRepository.getInstance(this);
    }

    public ScheduleNotificationCreator instantiateCreator()
    {
        return new ScheduleNotificationCreator();
    }

    public void onConnected()
    {
    }

    public void handleDestroy()
    {
        sendBroadcast(new Intent("RestartGradeService"));
    }
}


