package com.lonn.studentassistant.views.entityViews;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.implementations.professorEntity.ProfessorEntityActivity;
import com.lonn.studentassistant.databinding.CourseViewFullBinding;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.viewModels.CourseViewModel;
import com.lonn.studentassistant.views.abstractions.EntityView;

public class CourseViewFull extends EntityView<Course>
{
    CourseViewFullBinding binding;

    private CourseViewModel model;
    private Course entity;

    public CourseViewFull(Context context)
    {
        super(context);
    }

    public CourseViewFull(Context context, Course course)
    {
        super(context);
        entity = course;
        model = new CourseViewModel(entity);
        binding.setCourse(model);
    }

    public void inflateLayout(final Context context)
    {
        binding = DataBindingUtil.inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE), R.layout.course_view_full, this, true);

        this.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent professorIntent = new Intent(context, ProfessorEntityActivity.class);

                professorIntent.putExtra("professor", entity);

                context.startActivity(professorIntent);
            }
        });
    }

    public void update(Course newCourse)
    {
        Log.e("Updating course", newCourse.courseName);
        entity = newCourse;

        if (model == null)
        {
            model = new CourseViewModel(newCourse);
            binding.setCourse(model);
            Log.e("Creating course", model.courseName);
        }
        else
        {
            model.update(newCourse);
            Log.e("Updating professor", model.courseName);
        }

        model.notifyPropertyChanged(BR._all);
    }

    public String getEntityKey()
    {
        return entity!=null?entity.getKey():null;
    }
}
