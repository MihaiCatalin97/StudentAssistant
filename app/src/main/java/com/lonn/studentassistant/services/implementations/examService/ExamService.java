package com.lonn.studentassistant.services.implementations.examService;

import android.content.Intent;

import com.lonn.studentassistant.entities.Exam;
import com.lonn.studentassistant.entities.ScheduleClass;
import com.lonn.studentassistant.notifications.implementations.ExamNotificationCreator;
import com.lonn.studentassistant.notifications.implementations.ScheduleNotificationCreator;
import com.lonn.studentassistant.services.abstractions.DatabaseService;
import com.lonn.studentassistant.services.implementations.examService.dataAccessLayer.ExamRepository;

public class ExamService extends DatabaseService<Exam>
{
    public ExamRepository instantiateRepository()
    {
        return ExamRepository.getInstance(this);
    }

    public ExamNotificationCreator instantiateCreator()
    {
        return new ExamNotificationCreator();
    }

    public void onConnected()
    {
    }

    public void handleDestroy()
    {
        sendBroadcast(new Intent("RestartExamService"));
    }
}
