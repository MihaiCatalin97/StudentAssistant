package com.lonn.studentassistant.views.implementations.scrollViewLayouts;

import android.content.Context;
import android.util.AttributeSet;

import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.views.abstractions.ScrollViewLayout;
import com.lonn.studentassistant.views.implementations.endCategories.EndCategory;

import java.util.Collections;
import java.util.List;

public class CoursesPartialScrollView extends ScrollViewLayout<Course>
{
    public CoursesPartialScrollView(Context context)
    {
        super(context);
    }

    public CoursesPartialScrollView(Context context, AttributeSet set)
    {
        super(context, set);
    }

    public CoursesPartialScrollView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    protected List<String> getEntityNextCategory(Course course)
    {
        return Collections.singletonList("Courses");
    }

    protected EndCategory<Course> getBaseCategoryInstance(Context context)
    {
        return new EndCategory<>(context, "partial");
    }
}
