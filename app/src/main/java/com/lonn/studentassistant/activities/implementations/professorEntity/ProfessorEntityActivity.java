package com.lonn.studentassistant.activities.implementations.professorEntity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.ServiceBoundActivity;
import com.lonn.studentassistant.activities.implementations.professorEntity.callbacks.CourseCallback;
import com.lonn.studentassistant.activities.implementations.professorEntity.callbacks.OtherActivityCallback;
import com.lonn.studentassistant.activities.implementations.professorEntity.callbacks.ScheduleClassCallback;
import com.lonn.studentassistant.common.requests.GetByIdRequest;
import com.lonn.studentassistant.databinding.ProfessorEntityActivityLayoutBinding;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.OtherActivity;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.entities.ScheduleClass;
import com.lonn.studentassistant.services.implementations.coursesService.CourseService;
import com.lonn.studentassistant.services.implementations.otherActivityService.OtherActivityService;
import com.lonn.studentassistant.services.implementations.scheduleService.ScheduleClassService;
import com.lonn.studentassistant.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.views.abstractions.ScrollViewCategory;

public class ProfessorEntityActivity extends ServiceBoundActivity
{
    private ProfessorEntityActivityLayoutBinding binding;
    private boolean loadedRelatedEntities =false;
    private boolean editPrivilege;
    private Professor professor;

    private CourseCallback courseCallback = new CourseCallback(this);
    private OtherActivityCallback activityCallback = new OtherActivityCallback(this);
    private ScheduleClassCallback scheduleClassCallback;

    public ScrollViewCategory<Course> courseBaseCategory;
    public ScrollViewCategory<OtherActivity> otherActivityBaseCategory;
    public ScrollViewCategory<ScheduleClass> scheduleClassBaseCategory;


    protected void inflateLayout()
    {
        binding = DataBindingUtil.setContentView(this, R.layout.professor_entity_activity_layout);
    }

        @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(getIntent() != null && getIntent().getExtras() != null)
        {
            professor = (Professor) getIntent().getExtras().getSerializable("entity");

            if (professor != null)
            {
                ProfessorViewModel professorModel = new ProfessorViewModel(professor);
                binding.setProfessor(professorModel);
            }

            courseBaseCategory = findViewById(R.id.coursesBaseCategory);
            otherActivityBaseCategory = findViewById(R.id.otherActivitiesBaseCategory);
            scheduleClassBaseCategory = findViewById(R.id.scheduleBaseCategory);

            scheduleClassCallback = new ScheduleClassCallback(this, professor.getKey());
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();

        if(!loadedRelatedEntities)
        {
            for(String courseId : professor.courses)
                serviceConnections.postRequest(CourseService.class, new GetByIdRequest<Course>(courseId), courseCallback);

            for(String activityId : professor.otherActivities)
                serviceConnections.postRequest(OtherActivityService.class, new GetByIdRequest<OtherActivity>(activityId), activityCallback);

            for(String scheduleId : professor.scheduleClasses)
                serviceConnections.postRequest(ScheduleClassService.class, new GetByIdRequest<ScheduleClass>(scheduleId), scheduleClassCallback);

            loadedRelatedEntities = true;
        }
    }

    protected void unbindServices()
    {
        serviceConnections.unbind(courseCallback);
        serviceConnections.unbind(activityCallback);
        serviceConnections.unbind(scheduleClassCallback);
    }

    public void tapAdd(View v)
    {}
}
