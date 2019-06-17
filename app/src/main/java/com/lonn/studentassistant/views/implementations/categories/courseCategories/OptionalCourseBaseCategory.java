package com.lonn.studentassistant.views.implementations.courseCategories;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.activities.implementations.student.StudentActivity;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.views.abstractions.ScrollViewCategory;
import com.lonn.studentassistant.views.dialogBuilders.OptionalCourseDialogBuilder;

import java.util.List;

public class OptionalCourseBaseCategory extends ScrollViewCategory<Course>
{
    public OptionalCourseBaseCategory(Context context)
    {
        super(context);
    }

    public OptionalCourseBaseCategory(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public OptionalCourseBaseCategory(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OptionalCourseBaseCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean shouldContain(Course course)
    {
        return StudentActivity.getInstance().getStudentOptionalCourses().contains(course.getKey());
    }

    public void generateChildCategories(Course course)
    {
    }

    protected void initCategoryViewModel()
    {
        categoryViewModel.entityName = "optional course";
        categoryAddLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OptionalCourseDialogBuilder builder = new OptionalCourseDialogBuilder(getContext(), listAllEntities);
                builder.showDialog();
            }
        });

        categoryViewModel.notifyPropertyChanged(BR.entityName);
    }
}
