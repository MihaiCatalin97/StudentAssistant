package com.lonn.studentassistant.activities.abstractions;

import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.views.entityViews.EntityView;

public interface ICourseActivity extends IEntityActivity<Course>
{
    @Override
    EntityView<Course> getEntityViewInstance(Course entity);
}
