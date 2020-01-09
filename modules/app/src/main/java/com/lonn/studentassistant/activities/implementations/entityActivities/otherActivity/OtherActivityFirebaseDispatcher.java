package com.lonn.studentassistant.activities.implementations.entityActivities.otherActivity;

import android.util.Log;

import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.databinding.OtherActivityEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;

public class OtherActivityFirebaseDispatcher extends Dispatcher<OtherActivity> {
	private OtherActivityEntityActivityLayoutBinding binding;

	OtherActivityFirebaseDispatcher(OtherActivityEntityActivity entityActivity) {
		super(entityActivity);
		this.binding = entityActivity.binding;
	}

	void loadAll() {
		firebaseApi.getOtherActivityService()
				.getById(binding.getOtherActivity().getKey())
				.onComplete(binding::setOtherActivity,
						error -> {
							Log.e("Loading other activity", error.getMessage() == null ? "no message" : error.getMessage());
							entityActivity.showSnackBar("An error occurred while loading activity.");
						});
	}
}
