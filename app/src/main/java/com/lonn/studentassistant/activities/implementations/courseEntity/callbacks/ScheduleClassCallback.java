package com.lonn.studentassistant.activities.implementations.courseEntity.callbacks;

import com.lonn.studentassistant.activities.abstractions.callbacks.AbstractDatabaseCallback;
import com.lonn.studentassistant.activities.implementations.courseEntity.CourseEntityActivity;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.ScheduleClass;

public class ScheduleClassCallback extends AbstractDatabaseCallback<ScheduleClass>
{
    private String courseKey;

    public ScheduleClassCallback(CourseEntityActivity activity, String courseKey)
    {
        super(activity);
        this.courseKey = courseKey;
    }

    public void processResponse(CreateResponse<ScheduleClass> response)
    {
        if(isPersonalClass(response.getItems().get(0)))
            ((CourseEntityActivity)activity).scheduleBaseCategory.addOrUpdate(response.getItems().get(0));
    }

    public void processResponse(DeleteResponse<ScheduleClass> response)
    {
        if(isPersonalClass(response.getItems().get(0)))
            ((CourseEntityActivity)activity).scheduleBaseCategory.delete(response.getItems().get(0));
    }

    public void processResponse(EditResponse<ScheduleClass> response)
    {
        if(isPersonalClass(response.getItems().get(0)))
            ((CourseEntityActivity)activity).scheduleBaseCategory.addOrUpdate(response.getItems().get(0));
    }

    public void processResponse(GetByIdResponse<ScheduleClass> response)
    {
        if(isPersonalClass(response.getItems().get(0)))
            ((CourseEntityActivity)activity).scheduleBaseCategory.addOrUpdate(response.getItems().get(0));
    }

    public void processResponse(GetAllResponse<ScheduleClass> response)
    {
        for (ScheduleClass scheduleClass : response.getItems())
        {
            if(isPersonalClass(scheduleClass))
                ((CourseEntityActivity)activity).scheduleBaseCategory.addOrUpdate(scheduleClass);
        }
    }

    private boolean isPersonalClass(ScheduleClass scheduleClass)
    {
        return scheduleClass.courseKey.equals(courseKey);
    }
}
