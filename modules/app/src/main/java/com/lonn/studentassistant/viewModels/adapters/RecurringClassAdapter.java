package com.lonn.studentassistant.viewModels.adapters;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;
import com.lonn.studentassistant.viewModels.adapters.abstractions.ScheduleClassAdapter;
import com.lonn.studentassistant.viewModels.entities.RecurringClassViewModel;

public class RecurringClassAdapter extends ScheduleClassAdapter<RecurringClass, RecurringClassViewModel> {

    public RecurringClassAdapter(FirebaseConnectedActivity firebaseConnectedActivity) {
        super(firebaseConnectedActivity);
    }

    public RecurringClassViewModel adaptOne(RecurringClass recurringClass) {
        return super.adaptOne(RecurringClassViewModel.builder()
                .day(recurringClass.getDay())
                .build(), recurringClass);
    }
}
