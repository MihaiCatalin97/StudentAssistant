package com.lonn.studentassistant.activities.implementations.courseEntity.callbacks;

import com.lonn.studentassistant.activities.abstractions.callbacks.AbstractDatabaseCallback;
import com.lonn.studentassistant.activities.implementations.courseEntity.CourseEntityActivity;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.Professor;

public class ProfessorCallback extends AbstractDatabaseCallback<Professor>
{
    public ProfessorCallback(CourseEntityActivity activity)
    {
        super(activity);
    }

    public void processResponse(CreateResponse<Professor> response)
    {
        if (((CourseEntityActivity)activity).professorBaseCategory != null)
        {
            ((CourseEntityActivity)activity).professorBaseCategory.addOrUpdate(response.getItems().get(0));
        }
    }

    public void processResponse(DeleteResponse<Professor> response)
    {
        if (((CourseEntityActivity)activity).professorBaseCategory != null)
        {
            ((CourseEntityActivity)activity).professorBaseCategory.delete(response.getItems().get(0));
        }
    }

    public void processResponse(EditResponse<Professor> response)
    {
        if (((CourseEntityActivity)activity).professorBaseCategory != null)
        {
            ((CourseEntityActivity)activity).professorBaseCategory.addOrUpdate(response.getItems().get(0));
        }
    }

    public void processResponse(GetByIdResponse<Professor> response)
    {
        if (((CourseEntityActivity)activity).professorBaseCategory != null)
        {
            ((CourseEntityActivity)activity).professorBaseCategory.addOrUpdate(response.getItems().get(0));
        }
    }

    public void processResponse(GetAllResponse<Professor> response)
    {
        if (((CourseEntityActivity)activity).professorBaseCategory != null)
        {
            for (Professor professor : response.getItems())
                ((CourseEntityActivity)activity).professorBaseCategory.addOrUpdate(professor);
        }
    }
}
