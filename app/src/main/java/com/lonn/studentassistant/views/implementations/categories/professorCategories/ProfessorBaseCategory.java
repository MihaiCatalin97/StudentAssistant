package com.lonn.studentassistant.views.implementations.categories.professorCategories;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.views.abstractions.ScrollViewCategory;

public class ProfessorBaseCategory extends ScrollViewCategory<Professor>
{
    public ProfessorBaseCategory(Context context)
    {
        super(context);
    }

    public ProfessorBaseCategory(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ProfessorBaseCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProfessorBaseCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean shouldContain(Professor professor)
    {
        return true;
    }

    public void generateChildCategories(Professor professor)
    {
        if(generateChildCategories.equals("courses"))
        {
            Course auxCourse = new Course();

            for (String course : professor.courses)
            {
                auxCourse.setKey(course);

                if (children.get(auxCourse.courseName) == null)
                {
                    ProfessorCourseCategory newCategory = new ProfessorCourseCategory(getContext());
                    newCategory.setCategory(auxCourse.courseName);
                    addView(newCategory);
                    children.put(auxCourse.courseName, newCategory);
                }
            }

            sortChildren();
        }
    }

    protected void initCategoryViewModel()
    {
        categoryViewModel.entityName = "professor";

        categoryViewModel.notifyPropertyChanged(BR.entityName);
    }
}
