package com.lonn.studentassistant.views.implementations.categories.courseCategories;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.lonn.studentassistant.activities.abstractions.ServiceBoundActivity;
import com.lonn.studentassistant.activities.implementations.student.StudentActivity;
import com.lonn.studentassistant.entities.Course;

public class OptionalCourseEnrolledCategory extends OptionalCourseBaseCategory
{
    public OptionalCourseEnrolledCategory(Context context)
    {
        super(context);
    }

    public OptionalCourseEnrolledCategory(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public OptionalCourseEnrolledCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OptionalCourseEnrolledCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean shouldContain(Course course)
    {
        return ServiceBoundActivity.getCurrentActivity().getBusinessLayer().containsReferenceToEntity(course.getKey());
    }

    public void generateChildCategories(Course course)
    {}
}
