package com.lonn.studentassistant.views.implementations.otherActivityCategories;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.activities.implementations.student.StudentActivity;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.OtherActivity;
import com.lonn.studentassistant.views.abstractions.ScrollViewCategory;
import com.lonn.studentassistant.views.dialogBuilders.OptionalCourseDialogBuilder;

public class OtherActivityBaseCategory extends ScrollViewCategory<OtherActivity>
{
    public OtherActivityBaseCategory(Context context)
    {
        super(context);
    }

    public OtherActivityBaseCategory(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public OtherActivityBaseCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OtherActivityBaseCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean shouldContain(OtherActivity otherActivity)
    {
        return true;
    }

    public void generateChildCategories(OtherActivity otherActivity)
    {
        if(generateChildCategories.equals("types"))
        {
            if (children.get(otherActivity.type) == null)
            {
                OtherActivityTypeCategory newCategory = new OtherActivityTypeCategory(getContext());
                newCategory.setCategory(otherActivity.type);
                addView(newCategory);
                children.put(otherActivity.type, newCategory);
            }

            sortChildren();
        }
    }

    protected void initCategoryViewModel()
    {
        categoryViewModel.entityName = "other activity";

        categoryViewModel.notifyPropertyChanged(BR.entityName);
    }
}
