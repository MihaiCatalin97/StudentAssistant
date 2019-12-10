package com.lonn.studentassistant.views.implementations.categories.otherActivityCategories;

import android.content.Context;
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

    public boolean shouldContain(OtherActivity otherActivity) {
        return Utils.semesterToString(otherActivity.getSemester()).equals(viewModel.getCategoryTitle());
    }

    public void generateChildCategories(OtherActivity otherActivity) {
    }
}
