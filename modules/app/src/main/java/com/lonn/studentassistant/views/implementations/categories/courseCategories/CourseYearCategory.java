package com.lonn.studentassistant.views.implementations.categories.courseCategories;

import android.content.Context;
import android.util.AttributeSet;

import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.firebaselayer.entities.Course;

public class CourseYearCategory extends CourseBaseCategory {
    public CourseYearCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CourseYearCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean shouldContain(Course course) {
        return Utils.yearToString(course.getYear()).equals(viewModel.getCategoryTitle());
    }

    public void generateChildCategories(Course course) {
    }
}
