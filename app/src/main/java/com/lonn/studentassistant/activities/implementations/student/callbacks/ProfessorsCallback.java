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
import com.lonn.studentassistant.entities.Professor;

import java.util.Collections;

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
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Failed creating professor");
        else
        {
            if (response.getAction().equals("create"))
            {
                activity.professorsScrollViewLayout.addOrUpdate(response.getItems().get(0));
            }
        }
    }

    public void processResponse(DeleteResponse<Professor> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Failed deleting professor");
        else
        {
            if (response.getAction().equals("delete"))
            {
                activity.professorsScrollViewLayout.delete(response.getItems().get(0));
            }
        }
    }

    public void processResponse(EditResponse<Professor> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Failed editing professor");
        else
        {
            if (response.getAction().equals("edit"))
            {
                activity.professorsScrollViewLayout.addOrUpdate(response.getItems().get(0));
            }
        }
    }

    public void processResponse(GetByIdResponse<Professor> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Failed loading professor");
        else
        {
            if (response.getAction().equals("getById"))
            {
                activity.professorsScrollViewLayout.addOrUpdate(response.getItems().get(0));
            }
        }
    }

    public void processResponse(GetAllResponse<Professor> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Failed loading professors");
        else
        {
            if (response.getAction().equals("getAll"))
            {
                for(Professor professor : response.getItems())
                    activity.professorsScrollViewLayout.addOrUpdate(professor);
            }
        }
    }
}
