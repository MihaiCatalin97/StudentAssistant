package com.lonn.studentassistant.notifications.implementations;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.entities.Exam;
import com.lonn.studentassistant.entities.Grade;
import com.lonn.studentassistant.notifications.abstractions.NotificationCreator;

public class ExamNotificationCreator extends NotificationCreator<Exam>
{
    public ExamNotificationCreator()
    {
        smallIconId = R.mipmap.ic_launcher;
    }

    public String getTitle(DatabaseResponse<Exam> response)
    {
        return "Exam notification field1";
    }

    public String getText(DatabaseResponse<Exam> response)
    {
        return "Exam notification text";
    }
}
