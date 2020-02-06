package com.lonn.studentassistant.firebaselayer.businessLayer.adapters;

import com.lonn.studentassistant.firebaselayer.businessLayer.adapters.abstractions.DisciplineAdapter;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.OtherActivityViewModel;

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
