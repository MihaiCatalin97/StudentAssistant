package com.lonn.studentassistant.firebaselayer.adapters;

import com.lonn.studentassistant.firebaselayer.adapters.abstractions.ScheduleClassAdapter;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;

import static com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel.builder;

public class RecurringClassAdapter extends ScheduleClassAdapter<RecurringClass, RecurringClassViewModel> {
	public RecurringClassAdapter(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
	}

	public RecurringClassViewModel adapt(RecurringClass recurringClass) {
		return super.adapt(builder()
				.day(recurringClass.getDay())
				.build(), recurringClass);
	}

	public RecurringClass adapt(RecurringClassViewModel recurringClassViewModel) {
		return super.adapt(new RecurringClass()
						.setDay(recurringClassViewModel.getDayInt()),
				recurringClassViewModel);
	}
}
