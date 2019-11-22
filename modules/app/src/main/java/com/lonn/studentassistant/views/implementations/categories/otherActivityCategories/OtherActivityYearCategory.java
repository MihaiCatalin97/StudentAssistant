package com.lonn.studentassistant.views.implementations.categories.otherActivityCategories;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.firebaselayer.models.OtherActivity;

public class OtherActivityYearCategory extends OtherActivityBaseCategory {
    public OtherActivityYearCategory(Context context) {
        super(context);
    }

    public OtherActivityYearCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OtherActivityYearCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OtherActivityYearCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean shouldContain(OtherActivity otherActivity) {
        return Utils.yearToString(otherActivity.getYear()).equals(categoryViewModel.category);
    }

    public void generateChildCategories(OtherActivity otherActivity) {
    }
}
