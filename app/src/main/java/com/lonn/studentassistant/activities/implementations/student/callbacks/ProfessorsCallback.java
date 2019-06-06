package com.lonn.studentassistant.activities.implementations.student.callbacks;

import android.util.Log;

import com.lonn.studentassistant.activities.abstractions.IDatabaseCallback;
import com.lonn.studentassistant.activities.implementations.student.StudentActivity;
import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Professor;

public class ProfessorsCallback implements IDatabaseCallback<Professor>
{
    private StudentActivity activity;

    public ProfessorsCallback(StudentActivity activity)
    {
        this.activity = activity;
    }

    public void processResponse(DatabaseResponse<Professor> res)
    {
        Log.e("Default response","Received");
    }

    public void processResponse(CreateResponse<Professor> response)
    {

    }

    public void processResponse(DeleteResponse<Professor> response)
    {

    }

    public void processResponse(EditResponse<Professor> response)
    {

    }

    public void processResponse(GetByIdResponse<Professor> response)
    {

    }

    public void processResponse(GetAllResponse<Professor> response)
    {
        activity.hideSnackbar();

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Fail");
        else
        {
            if (response.getAction().equals("getAll"))
            {
                activity.professorAdapter.addAll(response.getItems());
            }
        }
    }
}
