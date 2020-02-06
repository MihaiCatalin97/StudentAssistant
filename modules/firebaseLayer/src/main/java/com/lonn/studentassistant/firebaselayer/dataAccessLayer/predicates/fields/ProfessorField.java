package com.lonn.studentassistant.firebaselayer.dataAccessLayer.predicates.fields;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Professor;

public class ProfessorField<T> extends BaseEntityField<Professor, T> {
	private ProfessorField(String fieldName) {
		super(fieldName);
	}
}
