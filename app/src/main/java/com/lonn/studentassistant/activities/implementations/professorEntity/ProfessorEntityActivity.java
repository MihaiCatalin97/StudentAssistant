package com.lonn.studentassistant.activities.implementations.professorEntity;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.ServiceBoundActivity;
import com.lonn.studentassistant.databinding.ProfessorEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.models.Course;
import com.lonn.studentassistant.firebaselayer.models.OtherActivity;
import com.lonn.studentassistant.firebaselayer.models.Professor;
import com.lonn.studentassistant.firebaselayer.models.ScheduleClass;
import com.lonn.studentassistant.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.views.abstractions.ScrollViewCategory;

public class ProfessorEntityActivity extends ServiceBoundActivity {
    public ScrollViewCategory<Course> courseBaseCategory;
    public ScrollViewCategory<OtherActivity> otherActivityBaseCategory;
    public ScrollViewCategory<ScheduleClass> scheduleClassBaseCategory;
    private ProfessorEntityActivityLayoutBinding binding;
    private ProfessorViewModel viewModel;
    private boolean editPrivilege;

    public void updateEntity(Professor professor) {
        if (viewModel == null) {
            viewModel = new ProfessorViewModel(professor);
            binding.setProfessor(viewModel);
        }
        else {
            viewModel.update(professor);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        businessLayer.refreshAll();
    }

    public void tapAdd(View v) {
    }

    protected void inflateLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.professor_entity_activity_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        courseBaseCategory = findViewById(R.id.coursesBaseCategory);
        otherActivityBaseCategory = findViewById(R.id.otherActivitiesBaseCategory);
        scheduleClassBaseCategory = findViewById(R.id.scheduleBaseCategory);
    }
}
