package com.lonn.studentassistant.views.implementations.endCategories;

import android.content.Context;

import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.OtherActivity;
import com.lonn.studentassistant.views.abstractions.EntityView;
import com.lonn.studentassistant.views.abstractions.ScrollViewEndCategory;
import com.lonn.studentassistant.views.entityViews.CourseViewPartial;
import com.lonn.studentassistant.views.entityViews.OtherActivityViewPartial;

public class OtherActivityPartialCategory extends ScrollViewEndCategory<OtherActivity>
{
    public OtherActivityPartialCategory(Context context)
    {
        super(context);
    }

    public EntityView<OtherActivity> getEntityViewInstance(OtherActivity activity)
    {
        return new OtherActivityViewPartial(getContext(), activity);
    }
}
