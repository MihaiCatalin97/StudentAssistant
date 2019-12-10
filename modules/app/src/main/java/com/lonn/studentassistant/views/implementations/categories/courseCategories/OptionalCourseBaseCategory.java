package com.lonn.studentassistant.views.implementations.categories.courseCategories;

import android.content.Context;
import android.util.AttributeSet;

import com.lonn.studentassistant.firebaselayer.entities.Course;

public class OptionalCourseBaseCategory extends CourseBaseCategory {
    public OptionalCourseBaseCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OptionalCourseBaseCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean shouldContain(Course course) {
        return true;
    }

    public void generateChildCategories(Course course) {
    }

    protected void initCategoryViewModel() {
        viewModel.setEntityName("optional discipline");

        viewModel.notifyPropertyChanged(com.lonn.studentassistant.BR.entityName);
    }
}
