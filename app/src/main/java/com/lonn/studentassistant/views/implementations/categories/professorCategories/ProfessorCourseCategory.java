package com.lonn.studentassistant.views.implementations.categories.professorCategories;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.lonn.studentassistant.firebaselayer.models.Course;
import com.lonn.studentassistant.firebaselayer.models.Professor;

public class ProfessorCourseCategory extends ProfessorBaseCategory {
    public ProfessorCourseCategory(Context context) {
        super(context);
    }

    public ProfessorCourseCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProfessorCourseCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProfessorCourseCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean shouldContain(Professor professor) {
        Course auxCourse = new Course();
        auxCourse.setCourseName(categoryViewModel.category);

        return professor.getCourses().contains(auxCourse.computeKey());
    }

    public void generateChildCategories(Professor professor) {
    }
}
