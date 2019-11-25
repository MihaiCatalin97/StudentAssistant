package com.lonn.studentassistant.views.implementations.categories.otherActivityCategories;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;

public class OtherActivitySemesterCategory extends OtherActivityBaseCategory {
    public OtherActivitySemesterCategory(Context context) {
        super(context);
    }

    public OtherActivitySemesterCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OtherActivitySemesterCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OtherActivitySemesterCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean shouldContain(OtherActivity otherActivity) {
        return Utils.semesterToString(otherActivity.getSemester()).equals(categoryViewModel.category);
    }

    public void generateChildCategories(OtherActivity otherActivity) {
    }
}
