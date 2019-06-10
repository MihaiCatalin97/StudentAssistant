package com.lonn.studentassistant.views.implementations.scrollViewLayouts;

import android.content.Context;
import android.util.AttributeSet;

import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.OtherActivity;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.views.abstractions.ScrollViewLayout;
import com.lonn.studentassistant.views.implementations.endCategories.EndCategory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class OtherActivityFullScrollView extends ScrollViewLayout<OtherActivity>
{

    public OtherActivityFullScrollView(Context context)
    {
        super(context);
    }

    public OtherActivityFullScrollView(Context context, AttributeSet set)
    {
        super(context, set);
    }

    public OtherActivityFullScrollView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    protected List<String> getEntityNextCategory(OtherActivity activity)
    {
        return Collections.singletonList(activity.type);
    }

    protected EndCategory<OtherActivity> getBaseCategoryInstance(Context context)
    {
        return new EndCategory<>(context,"full");
    }
}
