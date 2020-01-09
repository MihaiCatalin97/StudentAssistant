package com.lonn.studentassistant.activities.implementations.entityActivities.otherActivity;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.EntityActivity;
import com.lonn.studentassistant.databinding.OtherActivityEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;

public class OtherActivityEntityActivity extends EntityActivity<OtherActivityViewModel> {
    OtherActivityEntityActivityLayoutBinding binding;
    private OtherActivityFirebaseDispatcher dispatcher;
    private OtherActivityViewModel viewModel;


    protected void loadAll() {
        dispatcher.loadAll();
    }

    protected void inflateLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.other_activity_entity_activity_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = getEntityFromIntent(this.getIntent());
        binding.setOtherActivity(viewModel);

        dispatcher = new OtherActivityFirebaseDispatcher(this);
    }
}
