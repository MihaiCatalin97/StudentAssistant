package com.lonn.studentassistant.firebaselayer.businessLayer.adapters;

import com.lonn.studentassistant.firebaselayer.businessLayer.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.User;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.UserViewModel;

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
