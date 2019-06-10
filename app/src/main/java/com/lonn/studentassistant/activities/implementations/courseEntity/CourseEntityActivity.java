package com.lonn.studentassistant.activities.implementations.courseEntity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.IProfessorActivity;
import com.lonn.studentassistant.activities.abstractions.ServiceBoundActivity;
import com.lonn.studentassistant.activities.implementations.courseEntity.callbacks.ProfessorCallback;
import com.lonn.studentassistant.common.requests.GetByIdRequest;
import com.lonn.studentassistant.databinding.CourseEntityActivityLayoutBinding;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.services.implementations.professorService.ProfessorService;
import com.lonn.studentassistant.viewModels.CourseViewModel;
import com.lonn.studentassistant.views.entityViews.EntityView;
import com.lonn.studentassistant.views.implementations.scrollViewLayouts.ProfessorPartialScrollView;

public class CourseEntityActivity extends ServiceBoundActivity implements IProfessorActivity
{
    private boolean loadedProfessors = false;
    private boolean editPrivilege;
    private Course course;
    private ProfessorCallback professorCallback = new ProfessorCallback(this);
    public ProfessorPartialScrollView entitiesScrollViewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        CourseEntityActivityLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.course_entity_activity_layout);

        if(getIntent() != null && getIntent().getExtras() != null)
        {
            course = (Course) getIntent().getExtras().getSerializable("entity");

            if (course != null)
            {
                CourseViewModel courseViewModel = new CourseViewModel(course);
                binding.setCourse(courseViewModel);
            }

            entitiesScrollViewLayout = findViewById(R.id.scrollViewCourseEntities);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if(!loadedProfessors)
        {
            for(String professorId : course.professors)
            {
                serviceConnections.postRequest(ProfessorService.class, new GetByIdRequest<Professor>(professorId), professorCallback);
            }

            loadedProfessors = true;
        }
    }

    protected void unbindServices()
    {
        serviceConnections.unbind(professorCallback);
    }

    public EntityView<Professor> getEntityViewInstance(Professor professor)
    {
        return new EntityView<>(getBaseContext(), professor, "full");
    }
}
