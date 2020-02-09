package com.lonn.studentassistant.firebaselayer.businessLayer.viewModels;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.User;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.EntityViewModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public final class UserViewModel extends EntityViewModel<User> {
	@Bindable
	private String email, name, personUUID;
	private String fcmToken;
	private AccountType accountType;

	@Override
	public UserViewModel setKey(String key) {
		super.setKey(key);
		return this;
	}

	@Override
	public UserViewModel clone() {
		return (UserViewModel) super.clone();
	}
}
