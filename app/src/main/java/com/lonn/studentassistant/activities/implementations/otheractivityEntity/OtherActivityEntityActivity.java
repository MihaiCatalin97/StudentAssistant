package com.lonn.studentassistant.activities.implementations.otheractivityEntity;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.ServiceBoundActivity;
import com.lonn.studentassistant.databinding.OtherActivityEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.models.OtherActivity;
import com.lonn.studentassistant.firebaselayer.models.Professor;
import com.lonn.studentassistant.firebaselayer.models.ScheduleClass;
import com.lonn.studentassistant.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.views.abstractions.ScrollViewCategory;

public class OtherActivityEntityActivity extends ServiceBoundActivity {
    public ScrollViewCategory<Professor> professorBaseCategory;
    public ScrollViewCategory<ScheduleClass> scheduleClassBaseCategory;
    private OtherActivityEntityActivityLayoutBinding binding;
    private boolean loadedRelatedEntities = false;
    private boolean editPrivilege;
    private OtherActivity otherActivity;

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
            otherActivity = (OtherActivity) getIntent().getExtras().getSerializable("entity");

            if (otherActivity != null) {
                OtherActivityViewModel activityViewModel = new OtherActivityViewModel(otherActivity);
                binding.setOtherActivity(activityViewModel);
            }

            professorBaseCategory = findViewById(R.id.professorsBaseCategory);
            scheduleClassBaseCategory = findViewById(R.id.scheduleBaseCategory);

        }
    }
}
