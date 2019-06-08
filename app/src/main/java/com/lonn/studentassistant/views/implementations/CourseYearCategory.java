package com.lonn.studentassistant.views.implementations;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.CheckBox;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.views.abstractions.ScrollViewCategory;
import com.lonn.studentassistant.entities.Course;

import java.util.LinkedList;
import java.util.List;

public class CourseYearCategory extends ScrollViewCategory<Course>
{
    SparseArray<CourseSemesterCategory> categories = new SparseArray<>();

    public CourseYearCategory(Context context)
    {
        super(context);

        init(context);
    }

    public CourseYearCategory(Context context, AttributeSet attributeSet)
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
                categoryName = "First year";
                break;
            }
            case 2:
            {
                categoryName = "Second year";
                break;
            }
            case 3:
            {
                categoryName = "Third year";
                break;
            }
            case 4:
            {
                categoryName = "First master year";
                break;
            }
            case 5:
            {
                categoryName = "Second master year";
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

    public void add(List<Course> newCourses)
    {
        List<List<Course>> categoriesLists = new LinkedList<>();

        for(Course course : newCourses)
        {
            if(categoriesLists.size() <= course.semester)
            {
                int iterations = course.semester - categoriesLists.size();
                for(int i=0;i < iterations;i++)
                    categoriesLists.add(new LinkedList<Course>());
            }

            categoriesLists.get(course.semester-1).add(course);
        }

        for (int i=0;i<categoriesLists.size();i++)
        {
            CourseSemesterCategory category = categories.get(i);

            if (category == null)
            {
                category = new CourseSemesterCategory(getContext());
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

    public void delete(Course course)
    {
        categories.get(course.year).delete(course);
    }

    public void update(Course course)
    {
        categories.get(course.year).update(course);
    }

    public int getCategory()
    {
        return categoryInt;
    }
}
