package com.lonn.studentassistant.firebaselayer.adapters;

import com.lonn.studentassistant.firebaselayer.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.firebaselayer.entities.RegistrationToken;
import com.lonn.studentassistant.firebaselayer.viewModels.RegistrationTokenViewModel;

public class RegistrationTokenAdapter extends ViewModelAdapter<RegistrationToken, RegistrationTokenViewModel> {
	public RegistrationTokenViewModel adapt(RegistrationToken token) {
		return new RegistrationTokenViewModel()
				.setAccountType(token.getAccountType())
				.setKey(token.getKey())
				.setExpiresAt(token.getExpiresAt())
				.setToken(token.getToken());
	}

	public RegistrationToken adapt(RegistrationTokenViewModel tokenViewModel) {
		RegistrationToken result = new RegistrationToken()
				.setToken(tokenViewModel.getToken())
				.setExpiresAt(tokenViewModel.getExpiresAt())
				.setAccountType(tokenViewModel.getAccountType());

		if (tokenViewModel.getKey() != null) {
			result.setKey(tokenViewModel.getKey());
		}

		return result;
	}
}
