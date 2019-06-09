package com.lonn.studentassistant.activities.implementations.professorEntity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.ICourseActivity;
import com.lonn.studentassistant.activities.abstractions.ServiceBoundActivity;
import com.lonn.studentassistant.activities.implementations.professorEntity.callbacks.CourseCallback;
import com.lonn.studentassistant.common.abstractions.EntityManager;
import com.lonn.studentassistant.common.requests.GetByIdRequest;
import com.lonn.studentassistant.databinding.ProfessorEntityActivityLayoutBinding;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.services.implementations.coursesService.CourseService;
import com.lonn.studentassistant.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.views.entityViews.CourseViewFull;

public class ProfessorEntityActivity extends ServiceBoundActivity implements ICourseActivity
{
    private boolean loadedCourses =false;
    private boolean editPrivilege;
    private Professor professor;
    private CourseCallback courseCallback = new CourseCallback(this);
    public EntityManager<Course> courseManager;

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
                ProfessorViewModel professorModel = new ProfessorViewModel(professor);
                binding.setProfessor(professorModel);
            }

            courseManager = new EntityManager<>((ViewGroup)findViewById(R.id.layoutProfessorCourses), professor.courses, this);
        }
    }

    @Override
    public void onStart() {
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

    public CourseViewFull createView(Course course)
    {
        return new CourseViewFull(getBaseContext(), course);
    }
}
