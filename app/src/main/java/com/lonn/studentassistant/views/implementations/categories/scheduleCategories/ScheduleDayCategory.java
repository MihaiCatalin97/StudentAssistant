package com.lonn.studentassistant.views.implementations.scheduleCategories;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.entities.ScheduleClass;
import com.lonn.studentassistant.views.abstractions.ScrollViewCategory;

public class ScheduleDayCategory extends ScheduleBaseCategory
{
    public ScheduleDayCategory(Context context)
    {
        super(context);
    }

    public ScheduleDayCategory(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ScheduleDayCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScheduleDayCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean shouldContain(ScheduleClass scheduleClass)
    {
        return Utils.dayToString(scheduleClass.day).equals(categoryViewModel.category);
    }

    public void generateChildCategories(ScheduleClass scheduleClass)
    {}
}
