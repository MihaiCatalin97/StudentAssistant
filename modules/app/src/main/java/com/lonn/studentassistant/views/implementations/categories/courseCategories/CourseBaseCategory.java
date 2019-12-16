package com.lonn.studentassistant.views.implementations.categories.courseCategories;

import android.content.Context;
import android.util.AttributeSet;

import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.views.abstractions.category.ScrollViewCategory;

public class CourseBaseCategory extends ScrollViewCategory<Course> {
    public CourseBaseCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
