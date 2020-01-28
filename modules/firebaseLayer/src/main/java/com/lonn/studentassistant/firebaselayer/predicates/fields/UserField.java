package com.lonn.studentassistant.firebaselayer.predicates.fields;

import com.lonn.studentassistant.firebaselayer.entities.User;

public class UserField<T> extends BaseEntityField<User, T> {
	public static UserField<String> PERSON_UUID = new UserField<>("personUUID");

	private UserField(String fieldName) {
		super(fieldName);
	}
}