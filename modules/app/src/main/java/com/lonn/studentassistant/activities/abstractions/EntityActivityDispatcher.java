package com.lonn.studentassistant.activities.abstractions;

import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.firebaselayer.api.FirebaseApi;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.services.Service;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.logging.Logger;

import java.util.LinkedList;
import java.util.List;

import static com.lonn.studentassistant.BR.editing;

public abstract class EntityActivityDispatcher<T extends EntityViewModel<? extends BaseEntity>> {
	protected FirebaseApi firebaseApi;
	protected EntityActivity<T> activity;

	protected EntityActivityDispatcher(EntityActivity<T> activity) {
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

	public void update(T entity) {
		if (entity != activity.activityEntity) {
			activity.showSnackBar("Updating the " + getEntityName() + "...");

			getService()
					.save(entity)
					.onSuccess(none -> {
						activity.showSnackBar("Successfully updated the " + getEntityName(), 1000);
						activity.setActivityEntity(entity);
						activity.getBinding().setVariable(editing, false);
					})
					.onError(error -> activity.logAndShowErrorSnack("An error occurred while updating the " + getEntityName(),
							error,
							getLogger()));
		}
		else {
			activity.showSnackBar("No changes detected in the " + getEntityName(), 1000);
			activity.getBinding().setVariable(editing, false);
		}
	}

	public void delete(T entity) {
		activity.showSnackBar("Deleting the " + getEntityName() + "...");

		getService()
				.deleteById(entity.getKey())
				.onSuccess(none -> {
					activity.showSnackBar("Successfully deleted the " + getEntityName(), 1000);
					activity.onBackPressed();
				})
				.onError(error -> activity.logAndShowErrorSnack("An error occurred while deleting the " + getEntityName(),
						error,
						getLogger()));
	}

	public abstract Service<?, Exception, T> getService();

	public abstract String getEntityName();

	public abstract Logger getLogger();
}
