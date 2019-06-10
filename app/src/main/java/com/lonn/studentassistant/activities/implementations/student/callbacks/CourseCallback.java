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

import java.util.Collections;

public class CourseCallback implements IDatabaseCallback<Course>
{
    private StudentActivity activity;

    public CourseCallback(StudentActivity activity)
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
            activity.showSnackbar("Failed creating course");
        else
        {
            if (response.getAction().equals("create"))
            {
                activity.coursesFullScrollView.addOrUpdate(response.getItems().get(0));
            }
        }
    }

    public void processResponse(DeleteResponse<Course> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Failed deleting course");
        else
        {
            if (response.getAction().equals("delete"))
            {
                activity.coursesFullScrollView.delete(response.getItems().get(0));
            }
        }
    }

    public void processResponse(EditResponse<Course> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Failed editing course");
        else
        {
            if (response.getAction().equals("edit"))
            {
                activity.coursesFullScrollView.addOrUpdate(response.getItems().get(0));
            }
        }
    }

    public void processResponse(GetByIdResponse<Course> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Failed loading course");
        else
        {
            if (response.getAction().equals("getById"))
            {
                activity.coursesFullScrollView.addOrUpdate(response.getItems().get(0));
            }
        }
    }

    public void processResponse(GetAllResponse<Course> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Failed loading courses");
        else
        {
            if (response.getAction().equals("getAll"))
            {
                for(Course course : response.getItems())
                    activity.coursesFullScrollView.addOrUpdate(course);
            }
        }
    }
}
