package com.lonn.studentassistant.activities.implementations.student.callbacks;

import com.lonn.studentassistant.activities.abstractions.AbstractDatabaseCallback;
import com.lonn.studentassistant.activities.implementations.student.StudentBusinessLayer;
import com.lonn.studentassistant.activities.implementations.student.StudentActivity;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.Student;

public class StudentCallback extends AbstractDatabaseCallback<Student>
{
    public StudentCallback(StudentActivity activity, StudentBusinessLayer studentBusinessLayer)
    {
        super(activity, studentBusinessLayer);
    }

    public void processResponse(CreateResponse<Student> response)
    {
    }

    public void processResponse(DeleteResponse<Student> response)
    {
    }

    public void processResponse(EditResponse<Student> response)
    {
        Student student = response.getItems().get(0);
        businessLayer.editActivityEntity(student);
    }

    public void processResponse(GetByIdResponse<Student> response)
    {
    }

    public void processResponse(GetAllResponse<Student> response)
    {
    }
}
