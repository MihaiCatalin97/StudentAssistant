package com.lonn.studentassistant.activities.implementations.professorEntity.businessLayer;

import com.lonn.studentassistant.activities.abstractions.IBusinessLayer;
import com.lonn.studentassistant.activities.abstractions.callbacks.AbstractDatabaseCallback;
import com.lonn.studentassistant.activities.implementations.professorEntity.ProfessorEntityActivity;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.Course;

class CourseCallback extends AbstractDatabaseCallback<Course>
{
    CourseCallback(ProfessorEntityActivity activity, IBusinessLayer businessLayer)
    {
        super(activity, businessLayer);
    }

    public void processResponse(CreateResponse<Course> response)
    {
        if (((ProfessorEntityActivity)activity).courseBaseCategory != null && businessLayer.containsReferenceToEntity(response.getItems().get(0).getKey()))
        {
            ((ProfessorEntityActivity)activity).courseBaseCategory.addOrUpdate(response.getItems().get(0));
        }
    }

    public void processResponse(DeleteResponse<Course> response)
    {
        if (((ProfessorEntityActivity)activity).courseBaseCategory != null && businessLayer.containsReferenceToEntity(response.getItems().get(0).getKey()))
        {
            ((ProfessorEntityActivity)activity).courseBaseCategory.delete(response.getItems().get(0));
        }
    }

    public void processResponse(EditResponse<Course> response)
    {
        if (((ProfessorEntityActivity)activity).courseBaseCategory != null && businessLayer.containsReferenceToEntity(response.getItems().get(0).getKey()))
        {
            ((ProfessorEntityActivity)activity).courseBaseCategory.addOrUpdate(response.getItems().get(0));
        }
    }

    public void processResponse(GetByIdResponse<Course> response)
    {
        if (((ProfessorEntityActivity)activity).courseBaseCategory != null && businessLayer.containsReferenceToEntity(response.getItems().get(0).getKey()))
        {
            ((ProfessorEntityActivity)activity).courseBaseCategory.addOrUpdate(response.getItems().get(0));
        }
    }

    public void processResponse(GetAllResponse<Course> response)
    {
        if (((ProfessorEntityActivity)activity).courseBaseCategory != null)
        {
            for (Course course : response.getItems())
            {
                if(businessLayer.containsReferenceToEntity(course.getKey()))
                    ((ProfessorEntityActivity) activity).courseBaseCategory.addOrUpdate(course);
            }
        }
    }
}
