package com.lonn.studentassistant.firebaselayer.adapters;

import com.lonn.studentassistant.firebaselayer.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.firebaselayer.entities.Administrator;
import com.lonn.studentassistant.firebaselayer.viewModels.AdministratorViewModel;

public class AdministratorAdapter extends ViewModelAdapter<Administrator, AdministratorViewModel> {
	public AdministratorViewModel adapt(Administrator administrator) {
		return AdministratorViewModel.builder()
				.firstName(administrator.getFirstName())
				.lastName(administrator.getLastName())
				.email(administrator.getEmail())
				.phoneNumber(administrator.getPhoneNumber())
				.imageMetadataKey(administrator.getImageMetadataKey())
				.build();
	}

	public Administrator adapt(AdministratorViewModel administratorViewModel) {
		return new Administrator()
				.setFirstName(administratorViewModel.getFirstName())
				.setLastName(administratorViewModel.getLastName())
				.setEmail(administratorViewModel.getEmail())
				.setPhoneNumber(administratorViewModel.getPhoneNumber())
				.setImageMetadataKey(administratorViewModel.getImageMetadataKey());
	}
}
