package com.lonn.studentassistant.views.implementations.categories.otherActivityCategories;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;
import com.lonn.studentassistant.views.abstractions.ScrollViewCategory;

public class OtherActivityBaseCategory extends ScrollViewCategory<OtherActivity> {
    public OtherActivityBaseCategory(Context context) {
        super(context);
    }

    public OtherActivityBaseCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OtherActivityBaseCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OtherActivityBaseCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean shouldContain(OtherActivity otherActivity) {
        return true;
    }

    public void generateChildCategories(OtherActivity otherActivity) {
        if (generateChildCategories.equals("types")) {
            if (children.get(otherActivity.getType()) == null) {
                OtherActivityTypeCategory newCategory = new OtherActivityTypeCategory(getContext());
                newCategory.setCategory(otherActivity.getType());
                addView(newCategory);
                children.put(otherActivity.getType(), newCategory);
            }

            sortChildren();
        }
    }

    protected void initCategoryViewModel() {
        categoryViewModel.entityName = "other activity";

        categoryViewModel.notifyPropertyChanged(com.lonn.studentassistant.BR.entityName);
    }
}
