package com.lonn.studentassistant.firebaselayer.entities.abstractions;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.UUID;

public abstract class BaseEntity implements Serializable {
	@Exclude
	private String key;

	public BaseEntity() {
		this.key = UUID.randomUUID().toString();
	}

	protected BaseEntity(String key) {
		this.key = key;
	}

	@Exclude
	public String getKey() {
		return key;
	}

	@Exclude
	public BaseEntity setKey(String keyString) {
		key = keyString;
		return this;
	}
}
