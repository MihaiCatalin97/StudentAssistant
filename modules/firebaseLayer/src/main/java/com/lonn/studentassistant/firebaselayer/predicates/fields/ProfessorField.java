package com.lonn.studentassistant.firebaselayer.predicates.fields;

import com.lonn.studentassistant.firebaselayer.entities.Professor;

public class ProfessorField<T> extends BaseEntityField<Professor, T> {
    private ProfessorField(String fieldName) {
        super(fieldName);
    }
}
