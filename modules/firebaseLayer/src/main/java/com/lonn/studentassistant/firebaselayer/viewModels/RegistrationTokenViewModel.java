package com.lonn.studentassistant.firebaselayer.viewModels;

import com.lonn.studentassistant.firebaselayer.Utils;
import com.lonn.studentassistant.firebaselayer.entities.RegistrationToken;
import com.lonn.studentassistant.firebaselayer.entities.enums.AccountType;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

import java.util.Date;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import static com.lonn.studentassistant.firebaselayer.Utils.generateHashDigest;
import static java.util.UUID.randomUUID;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class RegistrationTokenViewModel extends EntityViewModel<RegistrationToken> {
	private String token;
	private AccountType accountType;
	private Date expiresAt;

	public RegistrationTokenViewModel() {
		String uuid = UUID.randomUUID().toString();

		String stringToHash = uuid.substring(uuid.length() - 6);
		setToken(stringToHash);

		expiresAt = new Date();
		expiresAt.setTime(expiresAt.getTime() + Utils.DAY_MILLISECONDS);
	}

	@Override
	public RegistrationTokenViewModel setKey(String key){
		super.setKey(key);
		return this;
	}

	public RegistrationTokenViewModel setToken(String token) {
		super.setKey(generateHashDigest(token));
		this.token = token;
		return this;
	}
}
