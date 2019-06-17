package com.lonn.studentassistant.views.implementations.scheduleCategories;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.entities.ScheduleClass;
import com.lonn.studentassistant.views.abstractions.ScrollViewCategory;
import com.lonn.studentassistant.views.implementations.otherActivityCategories.OtherActivityTypeCategory;

public class ScheduleBaseCategory extends ScrollViewCategory<ScheduleClass>
{
    public ScheduleBaseCategory(Context context)
    {
        super(context);
    }

    public ScheduleBaseCategory(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ScheduleBaseCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScheduleBaseCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean shouldContain(ScheduleClass scheduleClass)
    {
        return true;
    }

    public void generateChildCategories(ScheduleClass scheduleClass)
    {
        if(generateChildCategories.equals("days"))
        {
            String dayString = Utils.dayToString(scheduleClass.day);
            if (children.get(dayString) == null)
            {
                ScheduleDayCategory newCategory = new ScheduleDayCategory(getContext());
                newCategory.setCategory(dayString);
                addView(newCategory);
                children.put(dayString, newCategory);
            }
        }
    }

    protected void initCategoryViewModel()
    {
        categoryViewModel.entityName = "schedule class";

        categoryViewModel.notifyPropertyChanged(BR.entityName);
    }
}