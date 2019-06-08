package com.lonn.studentassistant.activities.implementations.professorEntity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.ServiceBoundActivity;
import com.lonn.studentassistant.activities.implementations.professorEntity.callbacks.CourseCallback;
import com.lonn.studentassistant.common.requests.GetByIdRequest;
import com.lonn.studentassistant.databinding.ProfessorEntityActivityLayoutBinding;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.services.implementations.coursesService.CourseService;
import com.lonn.studentassistant.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.views.entityViews.CourseView;

import java.util.ArrayList;
import java.util.List;

public class ProfessorEntityActivity extends ServiceBoundActivity
{
    private boolean loadedCourses =false;
    private boolean editPrivilege;
    public ProfessorViewModel professorModel;
    private Professor professor;
    private List<CourseView> courseViews;
    private CourseCallback courseCallback = new CourseCallback(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ProfessorEntityActivityLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.professor_entity_activity_layout);

        if(getIntent() != null && getIntent().getExtras() != null)
        {
            professor = (Professor) getIntent().getExtras().getSerializable("professor");

            if (professor != null)
            {
                professorModel = new ProfessorViewModel(professor);
                binding.setProfessor(professorModel);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!loadedCourses)
        {
            for(String courseId : professor.courses)
            {
                serviceConnections.postRequest(CourseService.class, new GetByIdRequest<Course>(courseId), courseCallback);
            }

            loadedCourses = true;
        }
    }

    protected void unbindServices()
    {
        serviceConnections.unbind(courseCallback);
    }

    public void updateCourses(Course course)
    {
        boolean exists = false;
        int index = 0;

        if(courseViews == null)
            courseViews = new ArrayList<>();

        for (int i = 0; i < courseViews.size(); i++)
        {
            if (courseViews.get(i).getEntityKey().equals(course.getKey()))
            {
                exists = true;
                index = i;
                break;
            }
        }

        if (exists)
        {
            courseViews.get(index).update(course);
        }
        else
        {
            CourseView newCourseView = new CourseView(getBaseContext(), course);
            courseViews.add(newCourseView);
            ((LinearLayout)findViewById(R.id.layoutProfessorCourses)).addView(newCourseView);
        }
    }
}
