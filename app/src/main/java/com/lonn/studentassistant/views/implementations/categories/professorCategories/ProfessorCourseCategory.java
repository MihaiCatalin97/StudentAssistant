package com.lonn.studentassistant.views.implementations.categories.professorCategories;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Professor;

public class ProfessorCourseCategory extends ProfessorBaseCategory
{
    public ProfessorCourseCategory(Context context)
    {
        super(context);
    }

    public ProfessorCourseCategory(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ProfessorCourseCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProfessorCourseCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean shouldContain(Professor professor)
    {
        Course auxCourse = new Course();
        auxCourse.courseName = categoryViewModel.category;

        return professor.courses.contains(auxCourse.getKey());
    }

    public void generateChildCategories(Professor professor)
    {}
}
