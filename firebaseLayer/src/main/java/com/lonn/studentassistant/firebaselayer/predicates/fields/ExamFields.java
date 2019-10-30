package com.lonn.studentassistant.firebaselayer.predicates.fields;

import com.lonn.studentassistant.firebaselayer.models.Exam;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ExamFields<T> extends EntityFields<Exam, T> {

    @Getter
    private String fieldName;
}