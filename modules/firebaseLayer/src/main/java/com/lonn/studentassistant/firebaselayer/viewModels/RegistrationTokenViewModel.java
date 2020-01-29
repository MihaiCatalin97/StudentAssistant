package com.lonn.studentassistant.firebaselayer.viewModels;

import com.lonn.studentassistant.firebaselayer.Utils;
import com.lonn.studentassistant.firebaselayer.entities.RegistrationToken;
import com.lonn.studentassistant.firebaselayer.entities.enums.AccountType;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import static java.util.UUID.randomUUID;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class RegistrationTokenViewModel extends EntityViewModel<RegistrationToken> {
	private String token;
	private AccountType accountType;
	private Date expiresAt;

	public RegistrationTokenViewModel() {
		setKey(randomUUID().toString());
		expiresAt = new Date();
		expiresAt.setTime(expiresAt.getTime() + Utils.DAY_MILLISECONDS);
	}

	@Override
	public RegistrationTokenViewModel setKey(String key) {
		super.setKey(key);
		token = getKey().substring(getKey().length() - 6);
		return this;
	}
}
