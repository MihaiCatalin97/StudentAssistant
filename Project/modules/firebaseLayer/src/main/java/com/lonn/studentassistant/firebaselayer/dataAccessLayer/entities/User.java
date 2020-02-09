package com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities;

import com.google.firebase.database.Exclude;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public final class User extends BaseEntity {
	@Exclude
	private String userId;
	private String personUUID;
	private String email, name;
	private String fcmToken;
	private AccountType accountType;

	@Exclude
	public String getUserId() {
		return userId;
	}

	public User setUserId(String userId) {
		super.setKey(userId);
		this.userId = userId;

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
