package com.lonn.studentassistant.activities.implementations.student.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.implementations.student.customViews.abstractions.ListViewCategory;
import com.lonn.studentassistant.entities.Course;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseSemesterCategory extends ListViewCategory<Course>
{
    Map<String, TextView> courses = new HashMap<>();

    public CourseSemesterCategory(Context context)
    {
        super(context);
        init(context);
    }

    public CourseSemesterCategory(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        init(context);
    }

    public void setCategory(int category)
    {
        this.categoryInt = category;
        String categoryName;

        switch (category)
        {
            case 1:
            {
                categoryName = "First semester";
                break;
            }
            case 2:
            {
                categoryName = "Second semester";
                break;
            }
            default:
            {
                categoryName = "Unknown category";
                break;
            }
        }

        ((CheckBox)findViewById(R.id.checkBoxCategory)).setText(categoryName);
    }

    public void inflateLayout(Context context)
    {
        inflate(context, R.layout.category_layout, this);
    }

    public void init(Context context)
    {
        super.init(context);

        initContent();
    }

    public void update(List<Course> newCourses)
    {
        for(Course course : newCourses)
        {
            TextView courseTextView = courses.get(course.getKey());

            if (courseTextView == null)
            {
                courseTextView = new TextView(getContext());
                courseTextView.setText(course.courseName);

                categoryContentLayout.addView(courseTextView);
                courses.put(course.getKey(), courseTextView);
            }
            else
                courseTextView.setText(course.courseName);
        }

        if (newCourses.size() > 0)
            setVisibility(View.VISIBLE);
    }

    public int getCategory()
    {
        return categoryInt;
    }
}
