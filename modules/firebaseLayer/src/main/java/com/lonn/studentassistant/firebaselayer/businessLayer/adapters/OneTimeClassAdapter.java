package com.lonn.studentassistant.firebaselayer.businessLayer.adapters;

import com.lonn.studentassistant.firebaselayer.businessLayer.adapters.abstractions.ScheduleClassAdapter;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.OneTimeClass;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.OneTimeClassViewModel;

public class OneTimeClassAdapter extends ScheduleClassAdapter<OneTimeClass, OneTimeClassViewModel> {
	public OneTimeClassAdapter(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
	}

	public OneTimeClassViewModel adapt(OneTimeClass oneTimeClass) {
		return super.adapt(OneTimeClassViewModel.builder()
				.date(oneTimeClass.getDate())
				.build(), oneTimeClass);
	}

	public OneTimeClass adapt(OneTimeClassViewModel viewModel) {
		return super.adapt(new OneTimeClass()
						.setDate(viewModel.getDate()),
				viewModel);
	}
}
