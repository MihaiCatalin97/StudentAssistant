package com.lonn.studentassistant.activities.implementations.professorEntity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.ICourseActivity;
import com.lonn.studentassistant.activities.abstractions.ServiceBoundActivity;
import com.lonn.studentassistant.activities.implementations.professorEntity.callbacks.CourseCallback;
import com.lonn.studentassistant.activities.implementations.professorEntity.callbacks.OtherActivityCallback;
import com.lonn.studentassistant.common.requests.GetByIdRequest;
import com.lonn.studentassistant.databinding.ProfessorEntityActivityLayoutBinding;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.OtherActivity;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.services.implementations.coursesService.CourseService;
import com.lonn.studentassistant.services.implementations.otherActivityService.OtherActivityService;
import com.lonn.studentassistant.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.views.entityViews.EntityView;
import com.lonn.studentassistant.views.implementations.scrollViewLayouts.CoursesPartialScrollView;
import com.lonn.studentassistant.views.implementations.scrollViewLayouts.OtherActivityPartialScrollView;

public class ProfessorEntityActivity extends ServiceBoundActivity implements ICourseActivity
{
    private boolean loadedRelatedEntities =false;
    private boolean editPrivilege;
    private Professor professor;

    private CourseCallback courseCallback = new CourseCallback(this);
    private OtherActivityCallback activityCallback = new OtherActivityCallback(this);

    public CoursesPartialScrollView coursePartialScroll;
    public OtherActivityPartialScrollView otherActivityPartialScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ProfessorEntityActivityLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.professor_entity_activity_layout);

        if(getIntent() != null && getIntent().getExtras() != null)
        {
            professor = (Professor) getIntent().getExtras().getSerializable("entity");

            if (professor != null)
            {
                ProfessorViewModel professorModel = new ProfessorViewModel(professor);
                binding.setProfessor(professorModel);
            }

            coursePartialScroll = findViewById(R.id.scrollViewProfessorEntities);
            otherActivityPartialScroll = findViewById(R.id.scrollViewActivityEntities);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if(!loadedRelatedEntities)
        {
            for(String courseId : professor.courses)
                serviceConnections.postRequest(CourseService.class, new GetByIdRequest<Course>(courseId), courseCallback);

            for(String activityId : professor.otherActivities)
                serviceConnections.postRequest(OtherActivityService.class, new GetByIdRequest<OtherActivity>(activityId), activityCallback);

            loadedRelatedEntities = true;
        }
    }

    protected void unbindServices()
    {
        serviceConnections.unbind(courseCallback);
        serviceConnections.unbind(activityCallback);
    }

    public EntityView<Course> getEntityViewInstance(Course course)
    {
        return new EntityView<>(getBaseContext(), course, "full");
    }
}
