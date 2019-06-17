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

        String thisClassName = getClass().getSimpleName().toLowerCase();

        if(thisClassName.startsWith("optionalcourse"))
        {
            categoryViewModel.entityName = "optional course";
            categoryAddLayout.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    OptionalCourseDialogBuilder builder = new OptionalCourseDialogBuilder(getContext(), (List<Course>)listAllEntities);
                    builder.showDialog();
                }
            });
        }
        else if(thisClassName.startsWith("otheractivity"))
            categoryViewModel.entityName = "other activity";
        else if(thisClassName.startsWith("professor"))
            categoryViewModel.entityName = "professor";
        else if(thisClassName.startsWith("schedule"))
            categoryViewModel.entityName = "schedule class";

        categoryViewModel.notifyPropertyChanged(BR.entityName);
    }

    public void generateChildCategories(Course course)
    {}
}
