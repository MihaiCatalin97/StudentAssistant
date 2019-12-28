package com.lonn.studentassistant.viewModels.adapters;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;
import com.lonn.studentassistant.viewModels.adapters.abstractions.DisciplineAdapter;
import com.lonn.studentassistant.viewModels.entities.OtherActivityViewModel;

public class OtherActivityAdapter extends DisciplineAdapter<OtherActivity, OtherActivityViewModel> {
    public OtherActivityAdapter(FirebaseConnectedActivity firebaseConnectedActivity) {
        super(firebaseConnectedActivity);
    }

    public OtherActivityViewModel adaptOne(OtherActivity otherActivity) {
        return super.adaptOne(OtherActivityViewModel.builder()
                .type(otherActivity.getType())
                .build(), otherActivity);
    }
}
