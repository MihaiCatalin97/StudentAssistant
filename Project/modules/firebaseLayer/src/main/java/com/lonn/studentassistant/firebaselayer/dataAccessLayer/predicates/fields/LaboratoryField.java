package com.lonn.studentassistant.firebaselayer.dataAccessLayer.predicates.fields;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Laboratory;

public class LaboratoryField<T> extends BaseEntityField<Laboratory, T> {
	public static LaboratoryField<String> COURSE_KEY = new LaboratoryField<>("course");

	private LaboratoryField(String fieldName) {
		super(fieldName);
	}
}