package com.lonn.studentassistant.notifications.implementations;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Student;
import com.lonn.studentassistant.notifications.abstractions.NotificationCreator;

public class StudentNotificationCreator extends NotificationCreator<Student>
{
    public StudentNotificationCreator()
    {
        smallIconId = R.mipmap.ic_launcher;
    }

    public String getTitle(DatabaseResponse<Student> response)
    {
        return "Student notification title";
    }

    public String getText(DatabaseResponse<Student> response)
    {
        return "Student notification text";
    }
}
