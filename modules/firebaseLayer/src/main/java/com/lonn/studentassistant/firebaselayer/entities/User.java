package com.lonn.studentassistant.firebaselayer.entities;

import com.google.firebase.database.Exclude;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User extends BaseEntity {
	@Exclude
	private String userId;
	private String personUUID;
	private String email, name, accountType;

	@Exclude
	public String getUserId() {
		return userId;
	}

	public User setUserId(String userId) {
		super.setKey(userId);
		userId = userId;

		return this;
	}

	@Exclude
	public String getKey() {
		return userId;
	}

	@Override
	public User setKey(String key) {
		super.setKey(key);
		userId = key;

		return this;
	}
}
