package com.lonn.studentassistant.activities.implementations.courseEntity;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.ServiceBoundActivity;
import com.lonn.studentassistant.activities.implementations.courseEntity.callbacks.ProfessorCallback;
import com.lonn.studentassistant.activities.implementations.courseEntity.callbacks.ScheduleClassCallback;
import com.lonn.studentassistant.common.requests.GetByIdRequest;
import com.lonn.studentassistant.databinding.CourseEntityActivityLayoutBinding;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.entities.ScheduleClass;
import com.lonn.studentassistant.services.implementations.professorService.ProfessorService;
import com.lonn.studentassistant.services.implementations.scheduleService.ScheduleClassService;
import com.lonn.studentassistant.viewModels.CourseViewModel;
import com.lonn.studentassistant.views.implementations.categories.professorCategories.ProfessorBaseCategory;
import com.lonn.studentassistant.views.implementations.categories.scheduleCategories.ScheduleBaseCategory;

public class CourseEntityActivity extends ServiceBoundActivity<Course>
{
    private CourseEntityActivityLayoutBinding binding;
    private boolean loadedProfessors = false;
    private boolean editPrivilege;
    private Course course;

    private ProfessorCallback professorCallback = new ProfessorCallback(this);
    private ScheduleClassCallback scheduleClassCallback;

    public ProfessorBaseCategory professorBaseCategory;
    public ScheduleBaseCategory scheduleBaseCategory;


    protected void inflateLayout()
    {
        binding = DataBindingUtil.setContentView(this, R.layout.course_entity_activity_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(getIntent() != null && getIntent().getExtras() != null)
        {
            course = (Course) getIntent().getExtras().getSerializable("entity");

            if (course != null)
            {
                CourseViewModel courseViewModel = new CourseViewModel(course);
                binding.setCourse(courseViewModel);
            }

            professorBaseCategory = findViewById(R.id.professorsBaseCategory);
            scheduleBaseCategory = findViewById(R.id.scheduleBaseCategory);

            scheduleClassCallback = new ScheduleClassCallback(this, course.getKey());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if(!loadedProfessors)
        {
            for(String professorId : course.professors)
                serviceConnections.postRequest(ProfessorService.class, new GetByIdRequest<Professor>(professorId), professorCallback);

            for(String scheduleId : course.scheduleClasses)
                serviceConnections.postRequest(ScheduleClassService.class, new GetByIdRequest<ScheduleClass>(scheduleId), scheduleClassCallback);

            loadedProfessors = true;
        }
    }

    protected void unbindServices()
    {
        serviceConnections.unbind(professorCallback);
        serviceConnections.unbind(scheduleClassCallback);
    }

    public void tapAdd(View v)
    {}

    public void updateEntity(Course e)
    { }
}
