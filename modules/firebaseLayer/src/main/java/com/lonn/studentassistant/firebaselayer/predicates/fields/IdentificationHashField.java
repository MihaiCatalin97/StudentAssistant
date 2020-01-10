package com.lonn.studentassistant.firebaselayer.predicates.fields;

import com.lonn.studentassistant.firebaselayer.entities.IdentificationHash;

public class IdentificationHashField<T> extends BaseEntityField<IdentificationHash, T> {
	public static IdentificationHashField<String> ENTITY_KEY = new IdentificationHashField<>("entityKey");

	private IdentificationHashField(String fieldName) {
		super(fieldName);
	}
}
