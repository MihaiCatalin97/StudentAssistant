package com.lonn.studentassistant.views.implementations.scrollViewLayouts;

import android.content.Context;
import android.util.AttributeSet;

import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.views.abstractions.ScrollViewLayout;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.views.implementations.intermediaryCategories.CourseYearCategory;

import java.util.LinkedList;
import java.util.List;

public class CoursesFullScrollView extends ScrollViewLayout<Course>
{
    public CoursesFullScrollView(Context context)
    {
        super(context);
    }

    public CoursesFullScrollView(Context context, AttributeSet set)
    {
        super(context, set);
    }

    public CoursesFullScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected List<String> getEntityNextCategory(Course course)
    {
        List<String> result = new LinkedList<>();

        result.add(Utils.yearToString(course.year));

        return result;
    }

    protected CourseYearCategory getBaseCategoryInstance(Context context)
    {
        return new CourseYearCategory(context);
    }
}
