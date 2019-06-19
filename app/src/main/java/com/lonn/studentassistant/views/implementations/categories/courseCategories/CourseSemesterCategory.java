package com.lonn.studentassistant.views.implementations.categories.courseCategories;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.entities.Course;

public class CourseSemesterCategory extends CourseBaseCategory
{
    public CourseSemesterCategory(Context context)
    {
        super(context);
    }

    public CourseSemesterCategory(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CourseSemesterCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CourseSemesterCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean shouldContain(Course course)
    {
        return Utils.semesterToString(course.semester).equals(categoryViewModel.category);
    }

    public void generateChildCategories(Course course)
    {}
}
