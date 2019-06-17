package com.lonn.studentassistant.views.implementations.otherActivityCategories;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.OtherActivity;
import com.lonn.studentassistant.views.abstractions.ScrollViewCategory;
import com.lonn.studentassistant.views.implementations.professorCategories.ProfessorCourseCategory;

public class OtherActivityTypeCategory extends OtherActivityBaseCategory
{
    public OtherActivityTypeCategory(Context context)
    {
        super(context);
    }

    public OtherActivityTypeCategory(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public OtherActivityTypeCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OtherActivityTypeCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean shouldContain(OtherActivity otherActivity)
    {
        return otherActivity.type.equals(categoryViewModel.category);
    }

    public void generateChildCategories(OtherActivity otherActivity)
    {
    }
}
