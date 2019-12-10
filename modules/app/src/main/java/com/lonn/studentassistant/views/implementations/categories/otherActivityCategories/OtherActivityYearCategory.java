package com.lonn.studentassistant.views.implementations.categories.otherActivityCategories;

import android.content.Context;
import android.util.AttributeSet;

import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;

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

    public boolean shouldContain(OtherActivity otherActivity) {
        return Utils.yearToString(otherActivity.getYear()).equals(viewModel.getCategoryTitle());
    }

    public void generateChildCategories(OtherActivity otherActivity) {
    }
}
