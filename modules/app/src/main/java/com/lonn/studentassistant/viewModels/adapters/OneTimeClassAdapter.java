package com.lonn.studentassistant.viewModels.adapters;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.entities.OneTimeClass;
import com.lonn.studentassistant.viewModels.adapters.abstractions.ScheduleClassAdapter;
import com.lonn.studentassistant.viewModels.entities.OneTimeClassViewModel;

public class OneTimeClassAdapter extends ScheduleClassAdapter<OneTimeClass, OneTimeClassViewModel> {

    public OneTimeClassAdapter(FirebaseConnectedActivity firebaseConnectedActivity) {
        super(firebaseConnectedActivity);
    }

    public OneTimeClassViewModel adaptOne(OneTimeClass oneTimeClass) {
        return super.adaptOne(OneTimeClassViewModel.builder()
                .date(oneTimeClass.getDate())
                .build(), oneTimeClass);
    }
}
