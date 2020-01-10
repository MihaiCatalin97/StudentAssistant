package com.lonn.studentassistant.firebaselayer.adapters;

import com.lonn.studentassistant.firebaselayer.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.firebaselayer.entities.User;
import com.lonn.studentassistant.firebaselayer.viewModels.UserViewModel;

public class UserAdapter extends ViewModelAdapter<User, UserViewModel> {
	public UserViewModel adapt(User user) {
		return UserViewModel.builder()
				.personUUID(user.getPersonUUID())
				.email(user.getEmail())
				.accountType(user.getAccountType())
				.name(user.getName())
				.build()
				.setKey(user.getKey());
	}

	public User adapt(UserViewModel userViewModel) {
		User result = new User()
				.setPersonUUID(userViewModel.getPersonUUID())
				.setAccountType(userViewModel.getAccountType())
				.setEmail(userViewModel.getEmail())
				.setName(userViewModel.getName());

		if (userViewModel.getKey() != null) {
			result.setKey(userViewModel.getKey());
		}

		return result;
	}
}
