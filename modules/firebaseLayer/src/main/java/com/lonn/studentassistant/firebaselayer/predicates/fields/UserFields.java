package com.lonn.studentassistant.firebaselayer.predicates.fields;

import com.lonn.studentassistant.firebaselayer.models.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class UserFields<T> extends EntityFields<User, T> {
    public static UserFields<String> IDENTIFICATION_HASH = new UserFields<>("personUUID");

    @Getter
    private String fieldName;
}