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
import com.lonn.studentassistant.entities.Course;

import java.util.Collections;

public class CourseCallback implements IDatabaseCallback<Course>
{
    private ProfessorEntityActivity activity;

    public CourseCallback(ProfessorEntityActivity activity)
    {
        this.activity = activity;
    }

    public void processResponse(DatabaseResponse<Course> res)
    {
        Log.e("Default response","Received");
    }

    public void processResponse(CreateResponse<Course> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Fail");
        else
        {
            if (response.getAction().equals("create") && activity.coursePartialScroll != null)
            {
                activity.coursePartialScroll.addOrUpdate(response.getItems().get(0));
            }
        }
    }

    public void processResponse(DeleteResponse<Course> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Fail");
        else
        {
            if (response.getAction().equals("delete") && activity.coursePartialScroll != null)
            {
                activity.coursePartialScroll.delete(response.getItems().get(0));
            }
        }
    }

    public void processResponse(EditResponse<Course> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Fail");
        else
        {
            if (response.getAction().equals("edit") && activity.coursePartialScroll != null)
            {
                activity.coursePartialScroll.addOrUpdate(response.getItems().get(0));
            }
        }
    }

    public void processResponse(GetByIdResponse<Course> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Fail");
        else
        {
            if (response.getAction().equals("getById") && activity.coursePartialScroll != null)
            {
                activity.coursePartialScroll.addOrUpdate(response.getItems().get(0));
            }
        }
    }

    public void processResponse(GetAllResponse<Course> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Fail");
        else
        {
            if (response.getAction().equals("getAll") && activity.coursePartialScroll != null)
            {
                for(Course course : response.getItems())
                    activity.coursePartialScroll.addOrUpdate(course);
            }
        }
    }
}
