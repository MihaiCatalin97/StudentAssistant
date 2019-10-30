package com.lonn.studentassistant.activities.implementations.student.callbacks;

import com.lonn.studentassistant.activities.abstractions.callbacks.AbstractDatabaseCallback;
import com.lonn.studentassistant.activities.implementations.student.StudentActivity;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.Professor;

class ProfessorsCallback extends AbstractDatabaseCallback<Professor>
{
    ProfessorsCallback(StudentActivity activity)
    {
        super(activity);
    }

    public void processResponse(CreateResponse<Professor> response)
    {
        ((StudentActivity)activity).professorsBaseCategory.addOrUpdate(response.getItems().get(0));
    }

    public void processResponse(DeleteResponse<Professor> response)
    {
        ((StudentActivity)activity).professorsBaseCategory.delete(response.getItems().get(0));
    }

    public void processResponse(EditResponse<Professor> response)
    {
        ((StudentActivity)activity).professorsBaseCategory.addOrUpdate(response.getItems().get(0));
    }

    public void processResponse(GetByIdResponse<Professor> response)
    {
        ((StudentActivity)activity).professorsBaseCategory.addOrUpdate(response.getItems().get(0));
    }

    public void processResponse(GetAllResponse<Professor> response)
    {
        for(Professor professor : response.getItems())
            ((StudentActivity)activity).professorsBaseCategory.addOrUpdate(professor);
    }
}
