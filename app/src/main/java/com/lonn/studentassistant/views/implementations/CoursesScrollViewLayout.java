package com.lonn.studentassistant.views.implementations;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.views.abstractions.ScrollViewLayout;
import com.lonn.studentassistant.entities.Course;

import java.util.LinkedList;
import java.util.List;

public class CoursesScrollViewLayout extends ScrollViewLayout<Course>
{
    SparseArray<CourseYearCategory> categories = new SparseArray<>();

    public CoursesScrollViewLayout(Context context)
    {
        super(context);
    }

    public CoursesScrollViewLayout(Context context, AttributeSet set)
    {
        super(context, set);
    }

    public CoursesScrollViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void inflateLayout(Context context)
    {
        inflate(context, R.layout.scrollview_layout, this);
        categoryContentLayout = findViewById(R.id.layoutCategoryContent);
    }

    public void add(List<Course> newCourses)
    {
        List<List<Course>> categoriesLists = new LinkedList<>();

        for(Course course : newCourses)
        {
            if(categoriesLists.size() <= course.year)
            {
                int iterations = course.year - categoriesLists.size();
                for(int i=0;i < iterations;i++)
                    categoriesLists.add(new LinkedList<Course>());
            }

            categoriesLists.get(course.year-1).add(course);
        }

        for (int i=0;i<categoriesLists.size();i++)
        {
            CourseYearCategory category = categories.get(i);

            if (category == null)
            {
                category = new CourseYearCategory(getContext());
                category.setCategory(i + 1);
                category.add(categoriesLists.get(i));

                categoryContentLayout.addView(category);
                categories.put(i, category);
            }
            else
            {
                category.add(categoriesLists.get(i));
            }
        }
    }

    public void update(Course course)
    {
        categories.get(course.year).update(course);
    }

    public void delete(Course course)
    {
        categories.get(course.year).delete(course);
    }
}
