package com.lonn.studentassistant.views.implementations.endCategories;

import android.content.Context;

import com.lonn.studentassistant.views.abstractions.EntityView;
import com.lonn.studentassistant.views.abstractions.ScrollViewEndCategory;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.views.entityViews.CourseViewFull;

public class CourseFullCategory extends ScrollViewEndCategory<Course>
{
    public CourseFullCategory(Context context)
    {
        super(context);
    }

    public EntityView<Course> getEntityViewInstance(Course course)
    {
        return new CourseViewFull(getContext(), course);
    }
}
