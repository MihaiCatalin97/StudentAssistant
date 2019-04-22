package com.lonn.studentassistant.services.implementations.coursesService;

import android.content.Intent;
import android.util.Log;

import com.lonn.studentassistant.common.requests.GetAllRequest;
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

    public void onConnected()
    {
        postRequest(new GetAllRequest<Course>());
    }

    public void handleDestroy()
    {
        sendBroadcast(new Intent("RestartCourseService"));
    }
}
