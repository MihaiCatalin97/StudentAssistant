package com.lonn.studentassistant.views.implementations;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.IEntityActivity;
import com.lonn.studentassistant.common.abstractions.EntityManager;
import com.lonn.studentassistant.views.abstractions.EntityView;
import com.lonn.studentassistant.views.abstractions.ScrollViewCategory;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.views.entityViews.CourseViewPartial;

import java.util.LinkedList;
import java.util.List;

public class CourseSemesterCategory extends ScrollViewCategory<Course> implements IEntityActivity<Course>
{
    private EntityManager<Course> manager;

    public CourseSemesterCategory(Context context)
    {
        super(context);
        init(context);

        manager = new EntityManager<>(categoryContentLayout, new LinkedList<String>(), this);
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

        ((TextView)findViewById(R.id.titleCategory)).setText(categoryName);
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
        for (Course course : newCourses)
        {
            manager.addOrUpdate(course);
        }
    }

    public int getCategory()
    {
        return categoryInt;
    }

    public void delete(Course course)
    {
        manager.delete(course);
    }

    public void update(Course course)
    {
        manager.addOrUpdate(course);
    }

    public EntityView<Course> createView(Course course)
    {
        return new CourseViewPartial(getContext(), course);
    }
}
