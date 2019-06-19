package com.lonn.studentassistant.activities.implementations.otheractivityEntity.callbacks;

import android.util.Log;

import com.lonn.studentassistant.activities.abstractions.AbstractDatabaseCallback;
import com.lonn.studentassistant.activities.abstractions.IDatabaseCallback;
import com.lonn.studentassistant.activities.implementations.courseEntity.CourseEntityActivity;
import com.lonn.studentassistant.activities.implementations.otheractivityEntity.OtherActivityEntityActivity;
import com.lonn.studentassistant.activities.implementations.professorEntity.ProfessorEntityActivity;
import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.ScheduleClass;

import java.util.Collections;

public class ScheduleClassCallback extends AbstractDatabaseCallback<ScheduleClass>
{
    private String otherActivityKey;

    public ScheduleClassCallback(OtherActivityEntityActivity activity, String otherActivityKey)
    {
        super(activity);
        this.otherActivityKey = otherActivityKey;
    }

    public void processResponse(CreateResponse<ScheduleClass> response)
    {
        if(isPersonalClass(response.getItems().get(0)))
            ((OtherActivityEntityActivity)activity).scheduleClassBaseCategory.addOrUpdate(response.getItems().get(0));
    }

    public void processResponse(DeleteResponse<ScheduleClass> response)
    {
        if(isPersonalClass(response.getItems().get(0)))
            ((OtherActivityEntityActivity)activity).scheduleClassBaseCategory.delete(response.getItems().get(0));
    }

    public void processResponse(EditResponse<ScheduleClass> response)
    {
        if(isPersonalClass(response.getItems().get(0)))
            ((OtherActivityEntityActivity)activity).scheduleClassBaseCategory.addOrUpdate(response.getItems().get(0));
    }

    public void processResponse(GetByIdResponse<ScheduleClass> response)
    {
        if(isPersonalClass(response.getItems().get(0)))
            ((OtherActivityEntityActivity)activity).scheduleClassBaseCategory.addOrUpdate(response.getItems().get(0));
    }

    public void processResponse(GetAllResponse<ScheduleClass> response)
    {
        for (ScheduleClass scheduleClass : response.getItems())
        {
            if(isPersonalClass(scheduleClass))
                ((OtherActivityEntityActivity)activity).scheduleClassBaseCategory.addOrUpdate(scheduleClass);
        }
    }

    private boolean isPersonalClass(ScheduleClass scheduleClass)
    {
        return scheduleClass.courseKey.equals(otherActivityKey);
    }
}

