package com.lonn.studentassistant.activities.implementations.courseEntity;

import android.os.Bundle;

import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.ServiceBoundActivity;
import com.lonn.studentassistant.activities.implementations.login.LoginActivity;
import com.lonn.studentassistant.databinding.CourseEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.models.Course;
import com.lonn.studentassistant.viewModels.CourseViewModel;
import com.lonn.studentassistant.views.implementations.categories.professorCategories.ProfessorBaseCategory;
import com.lonn.studentassistant.views.implementations.categories.scheduleCategories.ScheduleBaseCategory;

public class CourseEntityActivity extends ServiceBoundActivity {
    public ProfessorBaseCategory professorBaseCategory;
    public ScheduleBaseCategory scheduleBaseCategory;
    private CourseEntityActivityLayoutBinding binding;
    private boolean loadedProfessors = false;
    private boolean editPrivilege;
    private Course course;

    @Override
    public void onStart() {
        super.onStart();

        if (!loadedProfessors) {
            loadedProfessors = true;
        }

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            FirebaseAuth.getInstance().signOut();
            NavUtils.navigateUpFromSameTask(this);
        }
    }

    protected void inflateLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.course_entity_activity_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null && getIntent().getExtras() != null) {
            course = (Course) getIntent().getExtras().getSerializable("entity");

            if (course != null) {
                CourseViewModel courseViewModel = new CourseViewModel(course);
                binding.setCourse(courseViewModel);
            }

            professorBaseCategory = findViewById(R.id.professorsBaseCategory);
            scheduleBaseCategory = findViewById(R.id.scheduleBaseCategory);
        }
    }
}
