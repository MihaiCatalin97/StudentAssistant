package com.lonn.studentassistant.firebaselayer.businessLayer.adapters.abstractions;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.EntityViewModel;

import java.util.ArrayList;
import java.util.List;

public abstract class ViewModelAdapter<T extends BaseEntity, U extends EntityViewModel<T>> {

	public abstract U adapt(T entity);

	public List<U> adapt(List<? extends T> entities) {
		List<U> adaptedEntities = new ArrayList<>();

		for (T entity : entities) {
			adaptedEntities.add(adapt(entity));
		}

		return adaptedEntities;
	}

	public abstract T adapt(U viewModel);
}
