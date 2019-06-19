package com.lonn.studentassistant.notifications.implementations;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Grade;
import com.lonn.studentassistant.notifications.abstractions.NotificationCreator;

public class GradeNotificationCreator extends NotificationCreator<Grade>
{
    public GradeNotificationCreator()
    {
        smallIconId = R.mipmap.ic_launcher;
    }

    public String getTitle(DatabaseResponse<Grade> response)
    {
        return "Grade notification field1";
    }

    public String getText(DatabaseResponse<Grade> response)
    {
        return "Grade notification text";
    }
}
