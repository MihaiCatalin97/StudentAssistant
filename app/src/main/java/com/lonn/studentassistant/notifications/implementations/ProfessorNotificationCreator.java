package com.lonn.studentassistant.notifications.implementations;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.notifications.abstractions.NotificationCreator;

public class ProfessorNotificationCreator extends NotificationCreator<Professor>
{
    public ProfessorNotificationCreator()
    {
        smallIconId = R.mipmap.ic_launcher;
    }

    public String getTitle(DatabaseResponse<Professor> response)
    {
        return "Professor notification title";
    }

    public String getText(DatabaseResponse<Professor> response)
    {
        return "Professor notification text";
    }
}
