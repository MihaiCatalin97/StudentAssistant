package com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.Person;

import lombok.Getter;

@Getter
public abstract class PersonViewModel<T extends Person> extends FileAssociatedEntityViewModel<T> {
	protected String imageMetadataKey;

	public abstract PersonViewModel setImageMetadataKey(String imageMetadataKey);
}
