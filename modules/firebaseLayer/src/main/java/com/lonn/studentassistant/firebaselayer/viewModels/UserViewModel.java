package com.lonn.studentassistant.firebaselayer.viewModels;

import androidx.databinding.Bindable;

import com.google.firebase.auth.FirebaseUser;
import com.lonn.studentassistant.firebaselayer.entities.User;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

public class UserViewModel extends EntityViewModel<User> {
	@Bindable
	public String email, name;

	public UserViewModel(FirebaseUser user) {
		this.email = user.getEmail();
		this.name = user.getDisplayName();
	}
}
