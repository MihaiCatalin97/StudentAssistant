package com.lonn.studentassistant.activities.implementations;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.databinding.ProfessorEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.viewModels.adapters.ProfessorAdapter;
import com.lonn.studentassistant.viewModels.entities.CourseViewModel;
import com.lonn.studentassistant.viewModels.entities.OtherActivityViewModel;
import com.lonn.studentassistant.viewModels.entities.ProfessorViewModel;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;

public class ProfessorEntityActivity extends FirebaseConnectedActivity {
    public ScrollViewCategory<CourseViewModel> courseBaseCategory;
    public ScrollViewCategory<OtherActivityViewModel> otherActivityBaseCategory;
    private ProfessorEntityActivityLayoutBinding binding;
    private ProfessorViewModel viewModel;
    private boolean editPrivilege;
    private ProfessorAdapter professorAdapter = new ProfessorAdapter(this);

    public void updateEntity(Professor professor) {
        viewModel = professorAdapter.adapt(professor);
        binding.setProfessor(viewModel);
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
    }
}
