package com.lonn.studentassistant.views.implementations.categories.professorCategories;

import android.content.Context;
import android.util.AttributeSet;

import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.Professor;

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

    public boolean shouldContain(Professor professor) {
        Course auxCourse = new Course();
        auxCourse.setDisciplineName(viewModel.getCategoryTitle());

        return professor.getCourses().contains(auxCourse.getKey());
    }

    public void generateChildCategories(Professor professor) {
    }
}
