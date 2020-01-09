package com.lonn.studentassistant.firebaselayer.viewModels;

import androidx.databinding.Bindable;

import com.google.firebase.auth.FirebaseUser;
import com.lonn.studentassistant.firebaselayer.entities.User;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

public class UserViewModel extends EntityViewModel<User> {
	@Bindable
	public String email, name, personUUID;

	public UserViewModel(FirebaseUser user) {
		this.email = user.getEmail();
		this.name = user.getDisplayName();
	}

	@Override
	public UserViewModel setKey(String key) {
		super.setKey(key);
		return this;
	}
}
