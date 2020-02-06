package com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities;

import com.google.firebase.database.Exclude;
import com.lonn.studentassistant.firebaselayer.Utils;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType;

import java.util.Date;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import static com.lonn.studentassistant.firebaselayer.Utils.generateHashDigest;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public final class RegistrationToken extends BaseEntity {
	@Exclude
	private String token;
	private AccountType accountType;
	private Date expiresAt;

	public RegistrationToken() {
		String uuid = UUID.randomUUID().toString();

		String stringToHash = uuid.substring(uuid.length() - 6);
		setToken(stringToHash);

		expiresAt = new Date();
		expiresAt.setTime(expiresAt.getTime() + Utils.DAY_MILLISECONDS);
	}

	@Override
	public RegistrationToken setKey(String key){
		super.setKey(key);
		return this;
	}

	public RegistrationToken setToken(String token) {
		super.setKey(generateHashDigest(token));
		this.token = token;
		return this;
	}

	@Exclude
	public String getToken() {
		return token;
	}
}
