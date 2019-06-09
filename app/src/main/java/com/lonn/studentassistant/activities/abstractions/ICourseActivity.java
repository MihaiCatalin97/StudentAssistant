package com.lonn.studentassistant.activities.abstractions;

import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.views.abstractions.EntityView;

public interface ICourseActivity extends IEntityActivity<Course>
{
    @Override
    EntityView<Course> createView(Course entity);
}
