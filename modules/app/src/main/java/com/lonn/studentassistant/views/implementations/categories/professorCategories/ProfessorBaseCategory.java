package com.lonn.studentassistant.views.implementations.categories.professorCategories;

import android.content.Context;
import android.util.AttributeSet;

import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.views.abstractions.category.ScrollViewCategory;

public class ProfessorBaseCategory extends ScrollViewCategory<Professor> {

    public ProfessorBaseCategory(Context context) {
        super(context);
    }

    public ProfessorBaseCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProfessorBaseCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean shouldContain(Professor professor) {
        return true;
    }

    public void generateChildCategories(Professor professor) {
        if (getCategoryViewModel().childCategoryTypes.equals("courses")) {
            Course auxCourse = new Course();

            for (String course : professor.getCourses()) {
                auxCourse.setDisciplineName(course.replace("~", "."));

                if (children.get(auxCourse.getDisciplineName()) == null) {
                    ProfessorCourseCategory newCategory = new ProfessorCourseCategory(getContext());
                    newCategory.setCategory(auxCourse.getDisciplineName());
                    addView(newCategory);
                    children.put(auxCourse.getDisciplineName(), newCategory);
                }
            }

            sortChildren();
        }
    }

    protected void initCategoryViewModel() {
        categoryViewModel.entityName = "professor";

        categoryViewModel.notifyPropertyChanged(com.lonn.studentassistant.BR.entityName);
    }
}
