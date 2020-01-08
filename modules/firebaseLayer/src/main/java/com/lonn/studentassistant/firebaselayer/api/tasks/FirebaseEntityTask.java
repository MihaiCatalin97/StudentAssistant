package com.lonn.studentassistant.firebaselayer.api.tasks;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

public abstract class FirebaseEntityTask<T extends EntityViewModel<? extends BaseEntity>, V> extends FirebaseTask<T, V> {

	public FirebaseEntityTask(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
	}
}
