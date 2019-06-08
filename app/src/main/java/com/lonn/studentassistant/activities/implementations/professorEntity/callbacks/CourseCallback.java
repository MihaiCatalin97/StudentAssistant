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

    }

    public void processResponse(DeleteResponse<Course> response)
    {

    }

    public void processResponse(EditResponse<Course> response)
    {

    }

    public void processResponse(GetByIdResponse<Course> response)
    {

    }

    public void processResponse(GetAllResponse<Course> response)
    {
        activity.hideSnackbar();

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Fail");
        else
        {
            if (response.getAction().equals("getAll"))
            {
                Log.e("GetAllCalled","Ye");
                activity.courseManager.notifyCoursesChanged(response.getItems());
            }
        }
    }
}
