package com.lonn.studentassistant.activities.abstractions;

import com.lonn.studentassistant.firebaselayer.api.FirebaseApi;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

public abstract class Dispatcher<T extends BaseEntity> {
	protected FirebaseApi firebaseApi;
	protected EntityActivity<? extends EntityViewModel<T>> entityActivity;

	protected Dispatcher(EntityActivity<? extends EntityViewModel<T>> entityActivity) {
		this.entityActivity = entityActivity;
		firebaseApi = entityActivity.getFirebaseApi();
	}
}
