package com.lonn.studentassistant.firebaselayer.predicates.fields;

import com.lonn.studentassistant.firebaselayer.entities.IdentificationHash;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class IdentificationHashFields<T> extends EntityFields<IdentificationHash, T> {
    public static IdentificationHashFields<String> HASH =
            new IdentificationHashFields<>("id");
    public static IdentificationHashFields<String> ENTITY_KEY =
            new IdentificationHashFields<>("entityKey");

    @Getter
    private String fieldName;
}
