package com.lonn.studentassistant.views.implementations.endCategories;

import android.content.Context;

import com.lonn.studentassistant.views.abstractions.EntityView;
import com.lonn.studentassistant.views.abstractions.ScrollViewEndCategory;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.views.entityViews.CourseViewPartial;

public class CoursePartialCategory extends ScrollViewEndCategory<Course>
{
    public CoursePartialCategory(Context context)
    {
        super(context);
    }

    public EntityView<Course> getEntityViewInstance(Course course)
    {
        return new CourseViewPartial(getContext(), course);
    }
}
