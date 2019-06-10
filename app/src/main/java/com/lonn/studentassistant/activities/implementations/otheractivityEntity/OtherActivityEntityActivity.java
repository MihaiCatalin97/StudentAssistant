package com.lonn.studentassistant.activities.implementations.otheractivityEntity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.IOtherActivityActivity;
import com.lonn.studentassistant.activities.abstractions.ServiceBoundActivity;
import com.lonn.studentassistant.activities.implementations.otheractivityEntity.callbacks.ProfessorCallback;
import com.lonn.studentassistant.common.requests.GetByIdRequest;
import com.lonn.studentassistant.databinding.OtherActivityEntityActivityLayoutBinding;
import com.lonn.studentassistant.entities.OtherActivity;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.services.implementations.professorService.ProfessorService;
import com.lonn.studentassistant.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.views.entityViews.EntityView;
import com.lonn.studentassistant.views.implementations.scrollViewLayouts.ProfessorPartialScrollView;

public class OtherActivityEntityActivity extends ServiceBoundActivity implements IOtherActivityActivity
{
    private boolean loadedRelatedEntities =false;
    private boolean editPrivilege;
    private OtherActivity otherActivity;

    private ProfessorCallback professorCallback = new ProfessorCallback(this);

    public ProfessorPartialScrollView professorPartialScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        OtherActivityEntityActivityLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.other_activity_entity_activity_layout);

        if(getIntent() != null && getIntent().getExtras() != null)
        {
            otherActivity = (OtherActivity) getIntent().getExtras().getSerializable("entity");

            if (otherActivity != null)
            {
                OtherActivityViewModel activityViewModel = new OtherActivityViewModel(otherActivity);
                binding.setOtherActivity(activityViewModel);
            }

            professorPartialScroll = findViewById(R.id.scrollViewProfessorEntities);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if(!loadedRelatedEntities)
        {
            for(String professorId : otherActivity.professors)
                serviceConnections.postRequest(ProfessorService.class, new GetByIdRequest<Professor>(professorId), professorCallback);

            loadedRelatedEntities = true;
        }
    }

    protected void unbindServices()
    {
        serviceConnections.unbind(professorCallback);
    }

    public EntityView<OtherActivity> getEntityViewInstance(OtherActivity activity)
    {
        return new EntityView<>(getBaseContext(), activity, "full");
    }
}
