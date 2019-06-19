package com.lonn.studentassistant.views.implementations.categories.courseCategories;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.views.abstractions.ScrollViewCategory;

public class CourseBaseCategory extends ScrollViewCategory<Course>
{
    public CourseBaseCategory(Context context)
    {
        super(context);
    }

    public CourseBaseCategory(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CourseBaseCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CourseBaseCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean shouldContain(Course course)
    {
        return true;
    }

    protected void initCategoryViewModel()
    {
        categoryViewModel.entityName = "course";

        if(getClass().getSimpleName().toLowerCase().contains("enrolled"))
        {
            categoryViewModel.entityName = "optional course";
            categoryAddLayout.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                }
            });
        }

        categoryViewModel.notifyPropertyChanged(BR.entityName);
    }

    public void generateChildCategories(Course course)
    {}
}
