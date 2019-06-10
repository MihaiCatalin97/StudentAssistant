package com.lonn.studentassistant.activities.implementations.professorEntity.callbacks;

import android.util.Log;

import com.lonn.studentassistant.activities.abstractions.IDatabaseCallback;
import com.lonn.studentassistant.activities.implementations.professorEntity.ProfessorEntityActivity;
import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.OtherActivity;

import java.util.Collections;

public class OtherActivityCallback implements IDatabaseCallback<OtherActivity>
{
    private ProfessorEntityActivity activity;

    public OtherActivityCallback(ProfessorEntityActivity activity)
    {
        this.activity = activity;
    }

    public void processResponse(DatabaseResponse<OtherActivity> res)
    {
        Log.e("Default response","Received");
    }

    public void processResponse(CreateResponse<OtherActivity> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Fail");
        else
        {
            if (response.getAction().equals("create") && activity.otherActivityPartialScroll != null)
            {
                activity.otherActivityPartialScroll.addOrUpdate(response.getItems().get(0));
            }
        }
    }

    public void processResponse(DeleteResponse<OtherActivity> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Fail");
        else
        {
            if (response.getAction().equals("delete") && activity.otherActivityPartialScroll != null)
            {
                activity.otherActivityPartialScroll.delete(response.getItems().get(0));
            }
        }
    }

    public void processResponse(EditResponse<OtherActivity> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Fail");
        else
        {
            if (response.getAction().equals("edit") && activity.otherActivityPartialScroll != null)
            {
                activity.otherActivityPartialScroll.addOrUpdate(response.getItems().get(0));
            }
        }
    }

    public void processResponse(GetByIdResponse<OtherActivity> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Fail");
        else
        {
            if (response.getAction().equals("getById") && activity.otherActivityPartialScroll != null)
            {
                activity.otherActivityPartialScroll.addOrUpdate(response.getItems().get(0));
            }
        }
    }

    public void processResponse(GetAllResponse<OtherActivity> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Fail");
        else
        {
            if (response.getAction().equals("getAll") && activity.otherActivityPartialScroll != null)
            {
                for(OtherActivity course : response.getItems())
                    activity.otherActivityPartialScroll.addOrUpdate(course);
            }
        }
    }
}
