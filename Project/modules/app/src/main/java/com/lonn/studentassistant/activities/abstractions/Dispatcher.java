package com.lonn.studentassistant.activities.abstractions;

import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.firebaselayer.businessLayer.api.FirebaseApi;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.Person;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.EntityViewModel;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

public abstract class Dispatcher<T extends FirebaseConnectedActivity, V extends EntityViewModel<? extends Person>> {
	protected FirebaseApi firebaseApi;
	protected T activity;
	@Getter
	protected V currentProfile;

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

	public abstract boolean update(V profile);
}
