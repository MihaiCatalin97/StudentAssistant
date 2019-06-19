package com.lonn.studentassistant.activities.implementations.professorEntity.callbacks;

import com.lonn.studentassistant.activities.abstractions.AbstractDatabaseCallback;
import com.lonn.studentassistant.activities.implementations.professorEntity.ProfessorEntityActivity;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.Course;

public class CourseCallback extends AbstractDatabaseCallback<Course>
{
    public CourseCallback(ProfessorEntityActivity activity)
    {
        super(activity);
    }

    public void processResponse(CreateResponse<Course> response)
    {
        if (((ProfessorEntityActivity)activity).courseBaseCategory != null)
        {
            ((ProfessorEntityActivity)activity).courseBaseCategory.addOrUpdate(response.getItems().get(0));
        }
    }

    public void processResponse(DeleteResponse<Course> response)
    {
        if (((ProfessorEntityActivity)activity).courseBaseCategory != null)
        {
            ((ProfessorEntityActivity)activity).courseBaseCategory.delete(response.getItems().get(0));
        }
    }

    public void processResponse(EditResponse<Course> response)
    {
        if (((ProfessorEntityActivity)activity).courseBaseCategory != null)
        {
            ((ProfessorEntityActivity)activity).courseBaseCategory.addOrUpdate(response.getItems().get(0));
        }
    }

    public void processResponse(GetByIdResponse<Course> response)
    {
        if (((ProfessorEntityActivity)activity).courseBaseCategory != null)
        {
            ((ProfessorEntityActivity)activity).courseBaseCategory.addOrUpdate(response.getItems().get(0));
        }
    }

    public void processResponse(GetAllResponse<Course> response)
    {
        if (((ProfessorEntityActivity)activity).courseBaseCategory != null)
        {
            for(Course course : response.getItems())
                ((ProfessorEntityActivity)activity).courseBaseCategory.addOrUpdate(course);
        }
    }
}
