package com.lonn.studentassistant.services.implementations.gradeService;

import android.content.Intent;

import com.lonn.studentassistant.entities.Grade;
import com.lonn.studentassistant.notifications.implementations.GradeNotificationCreator;
import com.lonn.studentassistant.services.abstractions.DatabaseService;
import com.lonn.studentassistant.services.implementations.gradeService.dataAccessLayer.GradeRepository;

public class GradeService extends DatabaseService<Grade>
{
    public GradeRepository instantiateRepository()
    {
        return GradeRepository.getInstance(this);
    }

    public GradeNotificationCreator instantiateCreator()
    {
        return new GradeNotificationCreator();
    }

    public void onConnected()
    {
    }

    public void handleDestroy()
    {
        sendBroadcast(new Intent("RestartGradeService"));
    }
}
