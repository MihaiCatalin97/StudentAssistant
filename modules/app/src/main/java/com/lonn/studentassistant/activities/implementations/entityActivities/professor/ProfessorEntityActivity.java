package com.lonn.studentassistant.activities.implementations.entityActivities.professor;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.EntityActivity;
import com.lonn.studentassistant.databinding.ProfessorEntityActivityLayoutBinding;
import com.lonn.studentassistant.viewModels.entities.ProfessorViewModel;

public class ProfessorEntityActivity extends EntityActivity<ProfessorViewModel> {
    ProfessorEntityActivityLayoutBinding binding;
    private ProfessorViewModel viewModel;
    private ProfessorEntityActivityFirebaseDispatcher dispatcher;

    protected void loadAll() {
        dispatcher.loadAll();
    }

    protected void inflateLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.professor_entity_activity_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = getEntityFromIntent(this.getIntent());
        binding.setProfessor(viewModel);

        dispatcher = new ProfessorEntityActivityFirebaseDispatcher(this);
    }
}
