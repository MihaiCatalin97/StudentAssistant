package com.lonn.studentassistant.services.implementations.professorService;

import android.content.Intent;

import com.lonn.studentassistant.common.requests.GetAllRequest;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.notifications.implementations.ProfessorNotificationCreator;
import com.lonn.studentassistant.services.abstractions.DatabaseService;
import com.lonn.studentassistant.services.implementations.professorService.dataAccessLayer.ProfessorRepository;

public class ProfessorService extends DatabaseService<Professor>
{
    public ProfessorRepository instantiateRepository()
    {
        return ProfessorRepository.getInstance(this);
    }

    public ProfessorNotificationCreator instantiateCreator()
    {
        return new ProfessorNotificationCreator();
    }

    public void onConnected()
    {
    }

    public void handleDestroy()
    {
        sendBroadcast(new Intent("RestartProfessorService"));
    }
}
