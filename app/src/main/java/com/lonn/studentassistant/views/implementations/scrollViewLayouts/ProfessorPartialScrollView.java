package com.lonn.studentassistant.views.implementations.scrollViewLayouts;

import android.content.Context;
import android.util.AttributeSet;

import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.views.abstractions.ScrollViewCategory;
import com.lonn.studentassistant.views.abstractions.ScrollViewLayout;
import com.lonn.studentassistant.views.implementations.endCategories.EndCategory;

import java.util.Collections;
import java.util.List;

public class ProfessorPartialScrollView extends ScrollViewLayout<Professor>
{
    public ProfessorPartialScrollView(Context context)
    {
        super(context);
    }

    public ProfessorPartialScrollView(Context context, AttributeSet set)
    {
        super(context, set);
    }

    public ProfessorPartialScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected List<String> getEntityNextCategory(Professor entity)
    {
        return Collections.singletonList("Professors");
    }

    @Override
    protected ScrollViewCategory<Professor> getBaseCategoryInstance(Context context)
    {
        return new EndCategory<>(context, "partial");
    }
}
