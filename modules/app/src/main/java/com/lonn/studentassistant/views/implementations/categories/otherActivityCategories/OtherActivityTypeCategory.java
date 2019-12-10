package com.lonn.studentassistant.views.implementations.categories.otherActivityCategories;

import android.content.Context;
import android.util.AttributeSet;

import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;

public class OtherActivityTypeCategory extends OtherActivityBaseCategory {

    public OtherActivityTypeCategory(Context context) {
        super(context);
    }

    public OtherActivityTypeCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OtherActivityTypeCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean shouldContain(OtherActivity otherActivity) {
        return otherActivity.getType().equals(viewModel.getCategoryTitle());
    }

    public void generateChildCategories(OtherActivity otherActivity) {
    }
}
