package com.lonn.studentassistant.services.implementations.coursesService;

import android.content.Intent;

import com.lonn.studentassistant.notifications.implementations.CourseNotificationCreator;
import com.lonn.studentassistant.services.abstractions.DatabaseService;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.services.implementations.coursesService.dataAccessLayer.CourseRepository;

public class CourseService extends DatabaseService<Course>
{
    public CourseRepository instantiateRepository()
    {
        return CourseRepository.getInstance(this);
    }

    public CourseNotificationCreator instantiateCreator()
    {
        return new CourseNotificationCreator();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Intent intent = new Intent("RestartService");
        sendBroadcast(intent);
    }
}
