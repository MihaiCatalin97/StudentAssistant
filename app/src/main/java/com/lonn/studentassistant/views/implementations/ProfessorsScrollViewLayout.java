package com.lonn.studentassistant.views.implementations;

import android.content.Context;
import android.util.AttributeSet;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.views.abstractions.ScrollViewLayout;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.views.entityViews.ProfessorViewFull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfessorsScrollViewLayout extends ScrollViewLayout<Professor>
{
    Map<String, ProfessorViewFull> professors = new HashMap<>();

    public ProfessorsScrollViewLayout(Context context)
    {
        super(context);
    }

    public ProfessorsScrollViewLayout(Context context, AttributeSet set)
    {
        super(context, set);
    }

    public ProfessorsScrollViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void inflateLayout(Context context)
    {
        inflate(context, R.layout.scrollview_layout, this);
        categoryContentLayout = findViewById(R.id.layoutCategoryContent);
    }

    public void add(List<Professor> newProfessors)
    {
        Collections.sort(newProfessors);

        for(Professor professor : newProfessors)
        {
            ProfessorViewFull professorView = professors.get(professor.getKey());

            if (professorView == null)
            {
                professorView = new ProfessorViewFull(getContext(), professor);

                categoryContentLayout.addView(professorView);
                professors.put(professor.getKey(), professorView);
            }
            else if (professorView.entityDifferent(professor))
                professorView.update(professor);
        }
    }

    public void update(Professor professor)
    {
        professors.get(professor.getKey()).update(professor);
    }

    public void delete(Professor professor)
    {
        categoryContentLayout.removeView(professors.get(professor.getKey()));
        professors.remove(professor.getKey());
    }
}
