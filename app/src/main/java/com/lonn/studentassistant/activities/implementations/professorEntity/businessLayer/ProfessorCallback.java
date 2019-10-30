package com.lonn.studentassistant.activities.implementations.professorEntity.businessLayer;

import com.lonn.studentassistant.activities.abstractions.callbacks.AbstractDatabaseCallback;
import com.lonn.studentassistant.activities.implementations.professorEntity.ProfessorEntityActivity;
import com.lonn.studentassistant.activities.implementations.student.StudentActivity;
import com.lonn.studentassistant.activities.implementations.student.callbacks.StudentBusinessLayer;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.Professor;

public class ProfessorCallback extends AbstractDatabaseCallback<Professor>
{
    ProfessorCallback(ProfessorEntityActivity activity, ProfessorBusinessLayer businessLayer)
    {
        super(activity, businessLayer);
    }

    public void processResponse(CreateResponse<Professor> response)
    {
    }

    public void processResponse(DeleteResponse<Professor> response)
    {
    }

    public void processResponse(EditResponse<Professor> response)
    {
        Professor entity = response.getItems().get(0);
        businessLayer.editActivityEntity(entity);
    }

    public void processResponse(GetByIdResponse<Professor> response)
    {
        Professor entity = response.getItems().get(0);
        businessLayer.editActivityEntity(entity);
    }

    public void processResponse(GetAllResponse<Professor> response)
    {
    }
}
