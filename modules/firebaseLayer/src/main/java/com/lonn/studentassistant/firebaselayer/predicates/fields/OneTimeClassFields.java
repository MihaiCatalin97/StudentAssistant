package com.lonn.studentassistant.firebaselayer.predicates.fields;

import com.lonn.studentassistant.firebaselayer.entities.OneTimeClass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class OneTimeClassFields<T> extends EntityFields<OneTimeClass, T> {

    @Getter
    private String fieldName;
}