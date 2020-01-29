package com.lonn.studentassistant.firebaselayer.entities;

import com.lonn.studentassistant.firebaselayer.Utils;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.entities.enums.AccountType;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public final class RegistrationToken extends BaseEntity {
	private String token;
	private AccountType accountType;
	private Date expiresAt;

	public RegistrationToken() {
		token = getKey().substring(getKey().length() - 6);
		expiresAt = new Date();
		expiresAt.setTime(expiresAt.getTime() + Utils.DAY_MILLISECONDS);
	}
}
