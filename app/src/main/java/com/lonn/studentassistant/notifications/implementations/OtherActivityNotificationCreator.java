package com.lonn.studentassistant.notifications.implementations;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.entities.OtherActivity;
import com.lonn.studentassistant.notifications.abstractions.NotificationCreator;

public class OtherActivityNotificationCreator extends NotificationCreator<OtherActivity>
{
    public OtherActivityNotificationCreator()
    {
        smallIconId = R.mipmap.ic_launcher;
    }

    public String getTitle(DatabaseResponse<OtherActivity> response)
    {
        return "Other activity notification field1";
    }

    public String getText(DatabaseResponse<OtherActivity> response)
    {
        return "Other activity notification text";
    }
}
