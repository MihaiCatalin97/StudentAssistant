package com.lonn.studentassistant.notifications.implementations;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.entities.ScheduleClass;
import com.lonn.studentassistant.notifications.abstractions.NotificationCreator;

public class ScheduleNotificationCreator extends NotificationCreator<ScheduleClass>
{
    public ScheduleNotificationCreator()
    {
        smallIconId = R.mipmap.ic_launcher;
    }

    public String getTitle(DatabaseResponse<ScheduleClass> response)
    {
        return "Schedule notification field1";
    }

    public String getText(DatabaseResponse<ScheduleClass> response)
    {
        return "Schedule notification text";
    }
}
