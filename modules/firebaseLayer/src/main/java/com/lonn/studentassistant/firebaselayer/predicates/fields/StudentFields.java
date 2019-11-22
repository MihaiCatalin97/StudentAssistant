package com.lonn.studentassistant.firebaselayer.predicates.fields;

import com.lonn.studentassistant.firebaselayer.models.Student;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class StudentFields<T> extends EntityFields<Student, T> {
    public static StudentFields<String> ID = new StudentFields<>("id");

    @Getter
    private String fieldName;
}