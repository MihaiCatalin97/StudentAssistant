package com.lonn.studentassistant.views.implementations.endCategories;

import android.content.Context;
import android.util.AttributeSet;

import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.views.abstractions.EntityView;
import com.lonn.studentassistant.views.abstractions.ScrollViewEndCategory;
import com.lonn.studentassistant.views.entityViews.ProfessorViewPartial;

public class ProfessorPartialCategory extends ScrollViewEndCategory<Professor>
{
    public ProfessorPartialCategory(Context context)
    {
        super(context);
        init(context);
    }

    public ProfessorPartialCategory(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        init(context);
    }

    public EntityView<Professor> getEntityViewInstance(Professor professor)
    {
        return new ProfessorViewPartial(getContext(), professor);
    }
}
