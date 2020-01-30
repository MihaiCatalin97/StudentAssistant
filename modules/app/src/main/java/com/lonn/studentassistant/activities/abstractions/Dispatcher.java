package com.lonn.studentassistant.activities.abstractions;

import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.firebaselayer.api.FirebaseApi;

import java.util.LinkedList;
import java.util.List;

public abstract class Dispatcher<T extends FirebaseConnectedActivity> {
	protected FirebaseApi firebaseApi;
	protected T activity;

	protected Dispatcher(T activity) {
		this.activity = activity;
		firebaseApi = activity.getFirebaseApi();
	}

	public abstract void loadAll(String personId);

	protected void removeNonExistingEntities(BindableHashMap<?> hashMap, List<String> existingEntityKeys) {
		List<String> entitiesToBeRemoved = new LinkedList<>(hashMap.keySet());

		entitiesToBeRemoved.removeAll(existingEntityKeys);

		for (String entityToBeRemoved : entitiesToBeRemoved) {
			hashMap.remove(entityToBeRemoved);
		}
	}
}
