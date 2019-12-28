package com.lonn.studentassistant.firebaselayer.predicates.fields;

import com.lonn.studentassistant.firebaselayer.entities.Student;

public class StudentField<T> extends BaseEntityField<Student, T> {
    private StudentField(String fieldName) {
        super(fieldName);
    }
}