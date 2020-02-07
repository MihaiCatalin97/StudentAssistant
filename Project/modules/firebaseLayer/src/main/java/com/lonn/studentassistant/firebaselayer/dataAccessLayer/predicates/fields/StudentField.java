package com.lonn.studentassistant.firebaselayer.dataAccessLayer.predicates.fields;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Student;

public class StudentField<T> extends BaseEntityField<Student, T> {
	private StudentField(String fieldName) {
		super(fieldName);
	}
}