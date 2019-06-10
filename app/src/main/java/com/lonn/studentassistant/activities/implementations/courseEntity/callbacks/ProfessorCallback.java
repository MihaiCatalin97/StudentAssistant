package com.lonn.studentassistant.activities.implementations.courseEntity.callbacks;

import android.util.Log;

import com.lonn.studentassistant.activities.abstractions.IDatabaseCallback;
import com.lonn.studentassistant.activities.implementations.courseEntity.CourseEntityActivity;
import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.Professor;

import java.util.Collections;

public class ProfessorCallback implements IDatabaseCallback<Professor>
{
    private CourseEntityActivity activity;

    public ProfessorCallback(CourseEntityActivity activity)
    {
        this.activity = activity;
    }

    public void processResponse(DatabaseResponse<Professor> res)
    {
        Log.e("Default response", "Received");
    }

    public void processResponse(CreateResponse<Professor> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Failed creating professor");
        else
        {
            if (response.getAction().equals("create") && activity.entitiesScrollViewLayout != null)
            {
                activity.entitiesScrollViewLayout.addOrUpdate(response.getItems().get(0));
            }
        }
    }

    public void processResponse(DeleteResponse<Professor> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if (!response.getResult().equals("success"))
            activity.showSnackbar("Fail");
        else
        {
            if (response.getAction().equals("delete") && activity.entitiesScrollViewLayout != null)
            {
                activity.entitiesScrollViewLayout.delete(response.getItems().get(0));
            }
        }
    }

    public void processResponse(EditResponse<Professor> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if (!response.getResult().equals("success"))
            activity.showSnackbar("Fail");
        else
        {
            if (response.getAction().equals("edit") && activity.entitiesScrollViewLayout != null)
            {
                activity.entitiesScrollViewLayout.addOrUpdate(response.getItems().get(0));
            }
        }
    }

    public void processResponse(GetByIdResponse<Professor> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if (!response.getResult().equals("success"))
            activity.showSnackbar("Fail");
        else
        {
            if (response.getAction().equals("getById") && activity.entitiesScrollViewLayout != null)
            {
                activity.entitiesScrollViewLayout.addOrUpdate(response.getItems().get(0));
            }
        }
    }

    public void processResponse(GetAllResponse<Professor> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if (!response.getResult().equals("success"))
            activity.showSnackbar("Fail");
        else
        {
            if (response.getAction().equals("getAll") && activity.entitiesScrollViewLayout != null)
            {
                for (Professor professor : response.getItems())
                    activity.entitiesScrollViewLayout.addOrUpdate(professor);
            }
        }
    }
}
