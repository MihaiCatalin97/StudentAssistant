package com.lonn.studentassistant.views.implementations.categories.courseCategories;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.lonn.studentassistant.firebaselayer.models.Course;

public class OptionalCourseBaseCategory extends CourseBaseCategory {
    public OptionalCourseBaseCategory(Context context) {
        super(context);
    }

    public OptionalCourseBaseCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OptionalCourseBaseCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OptionalCourseBaseCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean shouldContain(Course course) {
        return true;
    }

    public void generateChildCategories(Course course) {
    }

    protected void initCategoryViewModel() {
        categoryViewModel.entityName = "optional course";

        categoryViewModel.notifyPropertyChanged(com.lonn.studentassistant.BR.entityName);
    }
}
