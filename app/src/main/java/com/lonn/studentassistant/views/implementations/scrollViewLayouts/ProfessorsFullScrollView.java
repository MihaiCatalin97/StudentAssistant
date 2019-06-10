package com.lonn.studentassistant.views.implementations.scrollViewLayouts;

import android.content.Context;
import android.util.AttributeSet;

import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.views.abstractions.ScrollViewLayout;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.views.implementations.endCategories.EndCategory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ProfessorsFullScrollView extends ScrollViewLayout<Professor>
{
    public ProfessorsFullScrollView(Context context)
    {
        super(context);
    }

    public ProfessorsFullScrollView(Context context, AttributeSet set)
    {
        super(context, set);
    }

    public ProfessorsFullScrollView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    protected List<String> getEntityNextCategory(Professor professor)
    {
        Collections.sort(professor.courses);
        List<String> result = new LinkedList<>();

        for(String courseKey : professor.courses)
        {
            Course auxCourse = new Course();
            auxCourse.setKey(courseKey);

            result.add(auxCourse.courseName);
        }

        return result;
    }

    protected EndCategory<Professor> getBaseCategoryInstance(Context context)
    {
        return new EndCategory<>(context,"full");
    }
}
