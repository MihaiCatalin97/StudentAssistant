package com.lonn.studentassistant.activities.implementations.entityActivities.course;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.EntityActivity;
import com.lonn.studentassistant.databinding.CourseEntityActivityLayoutBinding;
import com.lonn.studentassistant.viewModels.entities.CourseViewModel;

public class CourseEntityActivity extends EntityActivity<CourseViewModel> {
    CourseEntityActivityLayoutBinding binding;
    private CourseViewModel viewModel;
    private CourseEntityActivityFirebaseDispatcher dispatcher;

    protected void loadAll() {
        dispatcher.loadAll();
    }

    protected void inflateLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.course_entity_activity_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = getEntityFromIntent(this.getIntent());
        binding.setCourse(viewModel);

        dispatcher = new CourseEntityActivityFirebaseDispatcher(this);
    }
}
