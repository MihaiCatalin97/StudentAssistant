package com.lonn.studentassistant.views.implementations.courseCategories;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.views.abstractions.ScrollViewCategory;

public class CourseYearCategory extends CourseBaseCategory
{
    public CourseYearCategory(Context context)
    {
        super(context);
    }

    public CourseYearCategory(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CourseYearCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CourseYearCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean shouldContain(Course course)
    {
        return Utils.yearToString(course.year).equals(categoryViewModel.category);
    }

    public void generateChildCategories(Course course)
    {}
}
