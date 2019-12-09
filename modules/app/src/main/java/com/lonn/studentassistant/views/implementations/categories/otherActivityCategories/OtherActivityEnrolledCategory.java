package com.lonn.studentassistant.views.implementations.categories.otherActivityCategories;

import android.content.Context;
import android.util.AttributeSet;

import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;

public class OtherActivityEnrolledCategory extends OtherActivityBaseCategory {

    public OtherActivityEnrolledCategory(Context context) {
        super(context);
    }

    public OtherActivityEnrolledCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OtherActivityEnrolledCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean shouldContain(OtherActivity otherActivity) {
        return true;
    }

    public void generateChildCategories(OtherActivity otherActivity) {
    }
}
