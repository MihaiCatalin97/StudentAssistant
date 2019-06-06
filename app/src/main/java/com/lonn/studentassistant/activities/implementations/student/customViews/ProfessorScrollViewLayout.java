package com.lonn.studentassistant.activities.implementations.student.customViews;

import android.content.Context;
import android.util.AttributeSet;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.implementations.student.customViews.abstractions.ListViewMainLayout;
import com.lonn.studentassistant.entities.Professor;

import java.util.List;

public class ProfessorScrollViewLayout extends ListViewMainLayout<Professor>
{
    public ProfessorScrollViewLayout(Context context)
    {
        super(context);
    }

    public ProfessorScrollViewLayout(Context context, AttributeSet set)
    {
        super(context, set);
    }

    public ProfessorScrollViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void inflateLayout(Context context)
    {
        inflate(context, R.layout.scrollview_layout, this);
    }

    public void update(List<Professor> newProfessors)
    {

    }
}
