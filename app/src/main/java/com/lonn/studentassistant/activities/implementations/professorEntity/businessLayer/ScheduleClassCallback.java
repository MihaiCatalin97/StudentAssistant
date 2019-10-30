package com.lonn.studentassistant.activities.implementations.professorEntity.businessLayer;

import com.lonn.studentassistant.activities.abstractions.IBusinessLayer;
import com.lonn.studentassistant.activities.abstractions.callbacks.AbstractDatabaseCallback;
import com.lonn.studentassistant.activities.implementations.professorEntity.ProfessorEntityActivity;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.ScheduleClass;

class ScheduleClassCallback extends AbstractDatabaseCallback<ScheduleClass>
{
    private String professorKey;

    ScheduleClassCallback(ProfessorEntityActivity activity, IBusinessLayer businessLayer)
    {
        super(activity, businessLayer);
    }

    public void processResponse(CreateResponse<ScheduleClass> response)
    {
        if(businessLayer.containsReferenceToEntity(response.getItems().get(0).getKey()))
            ((ProfessorEntityActivity)activity).scheduleClassBaseCategory.addOrUpdate(response.getItems().get(0));
    }

    public void processResponse(DeleteResponse<ScheduleClass> response)
    {
        if(businessLayer.containsReferenceToEntity(response.getItems().get(0).getKey()))
            ((ProfessorEntityActivity)activity).scheduleClassBaseCategory.delete(response.getItems().get(0));
    }

    public void processResponse(EditResponse<ScheduleClass> response)
    {
        if(businessLayer.containsReferenceToEntity(response.getItems().get(0).getKey()))
            ((ProfessorEntityActivity)activity).scheduleClassBaseCategory.addOrUpdate(response.getItems().get(0));
    }

    public void processResponse(GetByIdResponse<ScheduleClass> response)
    {
        if(businessLayer.containsReferenceToEntity(response.getItems().get(0).getKey()))
            ((ProfessorEntityActivity)activity).scheduleClassBaseCategory.addOrUpdate(response.getItems().get(0));
    }

    public void processResponse(GetAllResponse<ScheduleClass> response)
    {
        for (ScheduleClass scheduleClass : response.getItems())
        {
            if(businessLayer.containsReferenceToEntity(scheduleClass.getKey()))
                ((ProfessorEntityActivity)activity).scheduleClassBaseCategory.addOrUpdate(scheduleClass);
        }
    }
}

