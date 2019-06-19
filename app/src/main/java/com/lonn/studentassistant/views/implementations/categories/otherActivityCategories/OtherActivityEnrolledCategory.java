package com.lonn.studentassistant.views.implementations.categories.otherActivityCategories;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.lonn.studentassistant.activities.abstractions.ServiceBoundActivity;
import com.lonn.studentassistant.activities.implementations.student.StudentActivity;
import com.lonn.studentassistant.entities.OtherActivity;

public class OtherActivityEnrolledCategory extends OtherActivityBaseCategory
{
    public OtherActivityEnrolledCategory(Context context)
    {
        super(context);
    }

    public OtherActivityEnrolledCategory(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public OtherActivityEnrolledCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OtherActivityEnrolledCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean shouldContain(OtherActivity otherActivity)
    {
        return ServiceBoundActivity.getCurrentActivity().getBusinessLayer().containsReferenceToEntity(otherActivity.getKey());
    }

    public void generateChildCategories(OtherActivity otherActivity)
    {}
}
