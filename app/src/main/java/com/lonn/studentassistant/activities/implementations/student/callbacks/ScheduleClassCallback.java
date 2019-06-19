package com.lonn.studentassistant.activities.implementations.student.callbacks;


import com.lonn.studentassistant.activities.abstractions.AbstractDatabaseCallback;
import com.lonn.studentassistant.activities.abstractions.IBusinessLayer;
import com.lonn.studentassistant.activities.implementations.student.StudentActivity;
import com.lonn.studentassistant.activities.implementations.student.StudentBusinessLayer;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.ScheduleClass;

public class ScheduleClassCallback extends AbstractDatabaseCallback<ScheduleClass>
{
    public ScheduleClassCallback(StudentActivity activity, IBusinessLayer businessLayer)
    {
        super(activity, businessLayer);
    }

    public void processResponse(CreateResponse<ScheduleClass> response)
    {
        if(((StudentBusinessLayer)businessLayer).isPersonalClass(response.getItems().get(0)))
            ((StudentActivity)activity).scheduleClassBaseCategory.addOrUpdate(response.getItems().get(0));
    }

    public void processResponse(DeleteResponse<ScheduleClass> response)
    {
        if(((StudentBusinessLayer)businessLayer).isPersonalClass(response.getItems().get(0)))
            ((StudentActivity)activity).scheduleClassBaseCategory.delete(response.getItems().get(0));
    }

    public void processResponse(EditResponse<ScheduleClass> response)
    {
        if(((StudentBusinessLayer)businessLayer).isPersonalClass(response.getItems().get(0)))
            ((StudentActivity)activity).scheduleClassBaseCategory.addOrUpdate(response.getItems().get(0));
    }

    public void processResponse(GetByIdResponse<ScheduleClass> response)
    {
        if(((StudentBusinessLayer)businessLayer).isPersonalClass(response.getItems().get(0)))
            ((StudentActivity)activity).scheduleClassBaseCategory.addOrUpdate(response.getItems().get(0));
    }

    public void processResponse(GetAllResponse<ScheduleClass> response)
    {
        for (ScheduleClass scheduleClass : response.getItems())
        {
            if (((StudentBusinessLayer)businessLayer).isPersonalClass(scheduleClass))
            {
                ((StudentActivity)activity).scheduleClassBaseCategory.addOrUpdate(scheduleClass);
            }
        }
    }
}
