package com.lonn.studentassistant.activities.implementations.professorEntity.businessLayer;

import com.lonn.studentassistant.activities.abstractions.IBusinessLayer;
import com.lonn.studentassistant.activities.abstractions.callbacks.AbstractDatabaseCallback;
import com.lonn.studentassistant.activities.implementations.professorEntity.ProfessorEntityActivity;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.OtherActivity;

class OtherActivityCallback extends AbstractDatabaseCallback<OtherActivity>
{
    OtherActivityCallback(ProfessorEntityActivity activity, IBusinessLayer businessLayer)
    {
        super(activity, businessLayer);
    }

    public void processResponse(CreateResponse<OtherActivity> response)
    {
        if (((ProfessorEntityActivity)activity).otherActivityBaseCategory != null && businessLayer.containsReferenceToEntity(response.getItems().get(0).getKey()))
        {
            ((ProfessorEntityActivity)activity).otherActivityBaseCategory.addOrUpdate(response.getItems().get(0));
        }
    }

    public void processResponse(DeleteResponse<OtherActivity> response)
    {
        if (((ProfessorEntityActivity)activity).otherActivityBaseCategory != null && businessLayer.containsReferenceToEntity(response.getItems().get(0).getKey()))
        {
            ((ProfessorEntityActivity)activity).otherActivityBaseCategory.delete(response.getItems().get(0));
        }
    }

    public void processResponse(EditResponse<OtherActivity> response)
    {
        if (((ProfessorEntityActivity)activity).otherActivityBaseCategory != null && businessLayer.containsReferenceToEntity(response.getItems().get(0).getKey()))
        {
            ((ProfessorEntityActivity)activity).otherActivityBaseCategory.addOrUpdate(response.getItems().get(0));
        }
    }

    public void processResponse(GetByIdResponse<OtherActivity> response)
    {
        if (((ProfessorEntityActivity)activity).otherActivityBaseCategory != null && businessLayer.containsReferenceToEntity(response.getItems().get(0).getKey()))
        {
            ((ProfessorEntityActivity)activity).otherActivityBaseCategory.addOrUpdate(response.getItems().get(0));
        }
    }

    public void processResponse(GetAllResponse<OtherActivity> response)
    {
        if (((ProfessorEntityActivity)activity).otherActivityBaseCategory != null)
        {
            for (OtherActivity course : response.getItems())
            {
                if(businessLayer.containsReferenceToEntity(course.getKey()))
                    ((ProfessorEntityActivity) activity).otherActivityBaseCategory.addOrUpdate(course);
            }
        }
    }
}
