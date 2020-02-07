package com.lonn.studentassistant.activities.abstractions;

import com.lonn.studentassistant.databinding.BindableHashMap;
import com.lonn.studentassistant.firebaselayer.businessLayer.api.FirebaseApi;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.abstractions.Service;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.logging.Logger;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

import static com.lonn.studentassistant.BR.editing;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidEmail;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidName;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidPhoneNumber;

public abstract class EntityActivityDispatcher<T extends EntityViewModel<? extends BaseEntity>> {
	protected FirebaseApi firebaseApi;
	protected EntityActivity<T> activity;
	@Getter
	protected T currentEntity;

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

	public boolean update(T entity) {
		if (!entity.equals(activity.activityEntity)) {
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

			return true;
		}
		else {
			activity.showSnackBar("No changes detected in the " + getEntityName(), 1000);
			activity.getBinding().setVariable(editing, false);
			return true;
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
