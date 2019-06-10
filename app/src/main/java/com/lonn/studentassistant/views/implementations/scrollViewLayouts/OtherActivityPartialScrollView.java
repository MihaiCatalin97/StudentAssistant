package com.lonn.studentassistant.views.implementations.scrollViewLayouts;

import android.content.Context;
import android.util.AttributeSet;

import com.lonn.studentassistant.entities.OtherActivity;
import com.lonn.studentassistant.views.abstractions.ScrollViewLayout;
import com.lonn.studentassistant.views.implementations.endCategories.OtherActivityPartialCategory;

import java.util.Collections;
import java.util.List;

public class OtherActivityPartialScrollView extends ScrollViewLayout<OtherActivity>
{
    public OtherActivityPartialScrollView(Context context)
    {
        super(context);
    }

    public OtherActivityPartialScrollView(Context context, AttributeSet set)
    {
        super(context, set);
    }

    public OtherActivityPartialScrollView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    protected List<String> getEntityNextCategory(OtherActivity activity)
    {
        return Collections.singletonList("Other activities");
    }

    protected OtherActivityPartialCategory getBaseCategoryInstance(Context context)
    {
        return new OtherActivityPartialCategory(context);
    }
}
