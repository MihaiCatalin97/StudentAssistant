package com.lonn.studentassistant.activities.implementations.professorEntity.callbacks;

import com.lonn.studentassistant.activities.abstractions.AbstractDatabaseCallback;
import com.lonn.studentassistant.activities.implementations.professorEntity.ProfessorEntityActivity;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.OtherActivity;

public class OtherActivityCallback extends AbstractDatabaseCallback<OtherActivity>
{
    public OtherActivityCallback(ProfessorEntityActivity activity)
    {
        super(activity);
    }

    public void processResponse(CreateResponse<OtherActivity> response)
    {
        if (((ProfessorEntityActivity)activity).otherActivityBaseCategory != null)
        {
            ((ProfessorEntityActivity)activity).otherActivityBaseCategory.addOrUpdate(response.getItems().get(0));
        }
    }

    public void processResponse(DeleteResponse<OtherActivity> response)
    {
        if (((ProfessorEntityActivity)activity).otherActivityBaseCategory != null)
        {
            ((ProfessorEntityActivity)activity).otherActivityBaseCategory.delete(response.getItems().get(0));
        }
    }

    public void processResponse(EditResponse<OtherActivity> response)
    {
        if (((ProfessorEntityActivity)activity).otherActivityBaseCategory != null)
        {
            ((ProfessorEntityActivity)activity).otherActivityBaseCategory.addOrUpdate(response.getItems().get(0));
        }
    }

    public void processResponse(GetByIdResponse<OtherActivity> response)
    {
        if (((ProfessorEntityActivity)activity).otherActivityBaseCategory != null)
        {
            ((ProfessorEntityActivity)activity).otherActivityBaseCategory.addOrUpdate(response.getItems().get(0));
        }
    }

    public void processResponse(GetAllResponse<OtherActivity> response)
    {
        if (((ProfessorEntityActivity)activity).otherActivityBaseCategory != null)
        {
            for(OtherActivity course : response.getItems())
                ((ProfessorEntityActivity)activity).otherActivityBaseCategory.addOrUpdate(course);
        }
    }
}
