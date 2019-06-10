package com.lonn.studentassistant.views.implementations.intermediaryCategories;

import android.content.Context;

import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.views.abstractions.ScrollViewIntermediaryCategory;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.views.implementations.endCategories.CoursePartialCategory;

import java.util.LinkedList;
import java.util.List;

public class CourseYearCategory extends ScrollViewIntermediaryCategory<Course>
{
    public CourseYearCategory(Context context)
    {
        super(context);
    }

    public List<String> getEntityNextCategories(Course course)
    {
        List<String> result = new LinkedList<>();

        result.add(Utils.semesterToString(course.semester));

        return result;
    }

    public CoursePartialCategory getSubCategoryInstance(Context context)
    {
        return new CoursePartialCategory(context);
    }
}
