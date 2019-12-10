package com.lonn.studentassistant.views.implementations.categories.courseCategories;

import android.content.Context;
import android.util.AttributeSet;

import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.views.abstractions.category.ScrollViewCategory;

public class CourseBaseCategory extends ScrollViewCategory<Course> {
    public CourseBaseCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CourseBaseCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean shouldContain(Course course) {
        return true;
    }

    public void generateChildCategories(Course course) {
    }

    protected void initCategoryViewModel() {
        viewModel.setEntityName("discipline");

        getContent().setOnAddTap(() -> {

        });

        viewModel.notifyPropertyChanged(com.lonn.studentassistant.BR.entityName);
    }
}
