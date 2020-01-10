package com.lonn.studentassistant.activities.abstractions;

import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.firebaselayer.api.FirebaseApi;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

import java.util.LinkedList;
import java.util.List;

public abstract class Dispatcher {
	protected FirebaseApi firebaseApi;
	protected FirebaseConnectedActivity activity;

	protected Dispatcher(FirebaseConnectedActivity activity) {
		this.activity = activity;
		firebaseApi = activity.getFirebaseApi();
	}

	protected void removeNonExistingEntities(BindableHashMap<?> hashMap, List<String> existingEntityKeys) {
		List<String> entitiesToBeRemoved = new LinkedList<>(hashMap.keySet());

		entitiesToBeRemoved.removeAll(existingEntityKeys);

		for (String entityToBeRemoved : entitiesToBeRemoved) {
			hashMap.remove(entityToBeRemoved);
		}
	}
}
