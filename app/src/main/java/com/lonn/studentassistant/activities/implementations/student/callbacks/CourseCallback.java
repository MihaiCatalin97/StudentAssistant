package com.lonn.studentassistant.activities.implementations.student.callbacks;

import com.lonn.studentassistant.activities.abstractions.callbacks.AbstractDatabaseCallback;
import com.lonn.studentassistant.activities.implementations.student.StudentActivity;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.Course;

class CourseCallback extends AbstractDatabaseCallback<Course>
{
    CourseCallback(StudentActivity activity)
    {
        super(activity);
    }

    public void processResponse(CreateResponse<Course> response)
    {
        Course course = response.getItems().get(0);

        ((StudentActivity) activity).coursesBaseCategory.addOrUpdate(course);

        if (course.pack != 0)
            ((StudentActivity) activity).coursesProfileCategory.addOrUpdate(course);
    }

    public void processResponse(DeleteResponse<Course> response)
    {
        Course course = response.getItems().get(0);

        ((StudentActivity) activity).coursesBaseCategory.delete(course);

        if (course.pack != 0)
            ((StudentActivity) activity).coursesProfileCategory.delete(course);

    }

    public void processResponse(EditResponse<Course> response)
    {
        Course course = response.getItems().get(0);

        ((StudentActivity) activity).coursesBaseCategory.addOrUpdate(course);

        if (course.pack != 0)
            ((StudentActivity) activity).coursesProfileCategory.addOrUpdate(course);

    }

    public void processResponse(GetByIdResponse<Course> response)
    {
        Course course = response.getItems().get(0);

        ((StudentActivity) activity).coursesBaseCategory.addOrUpdate(course);

        if (course.pack != 0)
            ((StudentActivity) activity).coursesProfileCategory.addOrUpdate(course);
    }

    public void processResponse(GetAllResponse<Course> response)
    {
        for (Course course : response.getItems())
        {
            ((StudentActivity) activity).coursesBaseCategory.addOrUpdate(course);

            if (course.pack != 0)
                ((StudentActivity) activity).coursesProfileCategory.addOrUpdate(course);
        }
    }
}
