package com.lonn.studentassistant.activities.implementations;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.databinding.OtherActivityEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;
import com.lonn.studentassistant.viewModels.adapters.OtherActivityAdapter;
import com.lonn.studentassistant.viewModels.entities.OtherActivityViewModel;
import com.lonn.studentassistant.viewModels.entities.ProfessorViewModel;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;

public class OtherActivityEntityActivity extends FirebaseConnectedActivity {
    public ScrollViewCategory<ProfessorViewModel> professorBaseCategory;
    private OtherActivityEntityActivityLayoutBinding binding;
    private boolean loadedRelatedEntities = false;
    private boolean editPrivilege;
    private OtherActivity otherActivity;
    private OtherActivityAdapter otherActivityAdapter = new OtherActivityAdapter(this);

    @Override
    public void onStart() {
        super.onStart();

        if (!loadedRelatedEntities) {
//            for(String professorId : otherActivity.professors)
//                serviceConnections.postRequest(ProfessorService.class, new GetByIdRequest<Professor>(professorId), professorCallback);
//
//            for(String scheduleId : otherActivity.scheduleClasses)
//                serviceConnections.postRequest(ScheduleClassService.class, new GetByIdRequest<ScheduleClass>(scheduleId), scheduleClassCallback);

            loadedRelatedEntities = true;
        }
    }

    protected void inflateLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.other_activity_entity_activity_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null && getIntent().getExtras() != null) {
            otherActivity = (OtherActivity) getIntent().getExtras().getSerializable("entityViewModel");

            if (otherActivity != null) {
                OtherActivityViewModel activityViewModel = otherActivityAdapter.adapt(otherActivity);
                binding.setOtherActivity(activityViewModel);
            }

            professorBaseCategory = findViewById(R.id.professorsBaseCategory);
        }
    }
}
