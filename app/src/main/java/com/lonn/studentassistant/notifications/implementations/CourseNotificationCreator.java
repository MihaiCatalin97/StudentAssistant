package com.lonn.studentassistant.notifications.implementations;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.notifications.abstractions.NotificationCreator;

public class CourseNotificationCreator extends NotificationCreator<Course>
{
    public CourseNotificationCreator()
    {
        smallIconId = R.mipmap.ic_launcher;
    }

    public String getTitle(DatabaseResponse<Course> response)
    {
        return "Course notification title";
    }

    public String getText(DatabaseResponse<Course> response)
    {
        return "Course notification text";
    }
}
