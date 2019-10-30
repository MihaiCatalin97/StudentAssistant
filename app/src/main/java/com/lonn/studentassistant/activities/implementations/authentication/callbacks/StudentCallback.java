package com.lonn.studentassistant.activities.implementations.authentication.callbacks;

import android.content.Intent;

import com.lonn.studentassistant.activities.abstractions.callbacks.AbstractDatabaseCallback;
import com.lonn.studentassistant.activities.implementations.authentication.AuthenticationActivity;
import com.lonn.studentassistant.activities.implementations.student.StudentActivity;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.Student;

public class StudentCallback extends AbstractDatabaseCallback<Student>
{
    public StudentCallback(AuthenticationActivity activity)
    {
        super(activity);
    }

    public void processResponse(CreateResponse<Student> response)
    {}

    public void processResponse(DeleteResponse<Student> response)
    {}

    public void processResponse(EditResponse<Student> response)
    {}

    public void processResponse(GetByIdResponse<Student> response)
    {
        activity.hideSnackbar();

        Intent studentActivityIntent = new Intent(activity, StudentActivity.class);
        studentActivityIntent.putExtra("user", response.getItems().get(0).accountId);
        studentActivityIntent.putExtra("student", response.getItems().get(0));
        activity.startActivity(studentActivityIntent);
    }

    public void processResponse(GetAllResponse<Student> response)
    {}
}
