package com.lonn.studentassistant.firebaselayer.businessLayer.adapters;

import com.lonn.studentassistant.firebaselayer.businessLayer.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.RegistrationToken;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.RegistrationTokenViewModel;

public class RegistrationTokenAdapter extends ViewModelAdapter<RegistrationToken, RegistrationTokenViewModel> {
	public RegistrationTokenViewModel adapt(RegistrationToken token) {
		return new RegistrationTokenViewModel()
				.setAccountType(token.getAccountType())
				.setExpiresAt(token.getExpiresAt())
				.setToken(token.getToken());
	}

	public RegistrationToken adapt(RegistrationTokenViewModel tokenViewModel) {
		RegistrationToken result = new RegistrationToken()
				.setToken(tokenViewModel.getToken())
				.setExpiresAt(tokenViewModel.getExpiresAt())
				.setAccountType(tokenViewModel.getAccountType());

		return result;
	}
}
