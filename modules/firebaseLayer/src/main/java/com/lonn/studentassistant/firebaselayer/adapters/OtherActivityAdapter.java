package com.lonn.studentassistant.firebaselayer.adapters;

import com.lonn.studentassistant.firebaselayer.adapters.abstractions.DisciplineAdapter;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;

public class OtherActivityAdapter extends DisciplineAdapter<OtherActivity, OtherActivityViewModel> {
	public OtherActivityViewModel adapt(OtherActivity otherActivity) {
		return super.adapt(OtherActivityViewModel.builder()
				.type(otherActivity.getType())
				.build(), otherActivity);
	}

	public OtherActivity adapt(OtherActivityViewModel otherActivityViewModel) {
		return super.adapt(new OtherActivity()
						.setType(otherActivityViewModel.getType()),
				otherActivityViewModel);
	}
}
