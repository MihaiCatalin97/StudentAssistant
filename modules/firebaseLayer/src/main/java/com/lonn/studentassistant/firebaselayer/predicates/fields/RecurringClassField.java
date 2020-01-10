package com.lonn.studentassistant.firebaselayer.predicates.fields;

import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;

public class RecurringClassField<T> extends BaseEntityField<RecurringClass, T> {
	public static RecurringClassField<String> DISCIPLINE = new RecurringClassField<>("discipline");

	private RecurringClassField(String fieldName) {
		super(fieldName);
	}
}
