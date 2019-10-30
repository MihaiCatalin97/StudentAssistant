package com.lonn.studentassistant.activities.implementations.professorEntity;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.ServiceBoundActivity;
import com.lonn.studentassistant.activities.implementations.professorEntity.businessLayer.ProfessorBusinessLayer;
import com.lonn.studentassistant.databinding.ProfessorEntityActivityLayoutBinding;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.OtherActivity;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.entities.ScheduleClass;
import com.lonn.studentassistant.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.views.abstractions.ScrollViewCategory;

public class ProfessorEntityActivity extends ServiceBoundActivity<Professor>
{
    private ProfessorEntityActivityLayoutBinding binding;
    private ProfessorViewModel viewModel;
    private boolean editPrivilege;

    public ScrollViewCategory<Course> courseBaseCategory;
    public ScrollViewCategory<OtherActivity> otherActivityBaseCategory;
    public ScrollViewCategory<ScheduleClass> scheduleClassBaseCategory;

    protected void inflateLayout()
    {
        binding = DataBindingUtil.setContentView(this, R.layout.professor_entity_activity_layout);
    }

    public void updateEntity(Professor professor)
    {
        if(viewModel == null)
        {
            viewModel = new ProfessorViewModel(professor);
            binding.setProfessor(viewModel);
        }
        else
            viewModel.update(professor);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(getIntent() != null && getIntent().getExtras() != null)
            businessLayer = new ProfessorBusinessLayer(this, (Professor) getIntent().getExtras().getSerializable("entity"));
        else
            businessLayer = new ProfessorBusinessLayer(this, null);

        courseBaseCategory = findViewById(R.id.coursesBaseCategory);
        otherActivityBaseCategory = findViewById(R.id.otherActivitiesBaseCategory);
        scheduleClassBaseCategory = findViewById(R.id.scheduleBaseCategory);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        businessLayer.refreshAll();
    }

    public void tapAdd(View v)
    {}
}
