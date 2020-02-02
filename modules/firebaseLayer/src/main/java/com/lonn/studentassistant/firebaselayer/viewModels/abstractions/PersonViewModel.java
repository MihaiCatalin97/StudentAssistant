package com.lonn.studentassistant.firebaselayer.viewModels.abstractions;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.Person;

import lombok.Getter;

@Getter
public abstract class PersonViewModel<T extends Person> extends FileAssociatedEntityViewModel<T> {
	protected String imageMetadataKey;

	public abstract PersonViewModel setImageMetadataKey(String imageMetadataKey);
}
