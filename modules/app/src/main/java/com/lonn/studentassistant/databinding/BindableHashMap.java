package com.lonn.studentassistant.databinding;

import androidx.databinding.ViewDataBinding;

import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

import java.util.Collection;
import java.util.HashMap;

public class BindableHashMap<V extends EntityViewModel> extends HashMap<String, V> {
	private ViewDataBinding binding;
	private int property;

	public BindableHashMap(ViewDataBinding binding, int property) {
		this.binding = binding;
		this.property = property;
	}

	public BindableHashMap(ViewDataBinding binding, int property, Collection<V> entities) {
		this.binding = binding;
		this.property = property;

		for (V entity : entities) {
			super.put(entity.getKey(), entity);
		}

		binding.setVariable(property, this);
	}

	public void put(V entity) {
		put(entity.getKey(), entity);

		binding.setVariable(property, this);
	}

	@Override
	public V remove(Object entityKey) {
		V result = super.remove(entityKey);

		binding.setVariable(property, this);

		return result;
	}

	public void remove(V entity) {
		remove(entity.getKey());
	}
}
