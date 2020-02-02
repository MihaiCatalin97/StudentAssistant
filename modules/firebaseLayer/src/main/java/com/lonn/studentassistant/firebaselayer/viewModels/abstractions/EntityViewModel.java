package com.lonn.studentassistant.firebaselayer.viewModels.abstractions;

import androidx.databinding.BaseObservable;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.entities.enums.AccountType;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public abstract class EntityViewModel<T extends BaseEntity> extends BaseObservable implements Serializable, Cloneable {
	private String key;

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof EntityViewModel)) {
			return false;
		}
		return this.key.equals(((EntityViewModel) obj).getKey());
	}

	@Override
	public EntityViewModel<T> clone() {
		try {
			return (EntityViewModel<T>) super.clone();
		}
		catch (CloneNotSupportedException exception) {
			return null;
		}
	}
}
