package com.lonn.studentassistant.firebaselayer.adapters;

import com.lonn.studentassistant.firebaselayer.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.firebaselayer.entities.Administrator;
import com.lonn.studentassistant.firebaselayer.viewModels.AdministratorViewModel;

public class AdministratorAdapter extends ViewModelAdapter<Administrator, AdministratorViewModel> {
	public AdministratorViewModel adapt(Administrator administrator) {
		return AdministratorViewModel.builder()
				.firstName(administrator.getFirstName())
				.lastName(administrator.getLastName())
				.administratorKey(administrator.getAdministratorKey())
				.build();
	}

	public Administrator adapt(AdministratorViewModel administratorViewModel) {
		return new Administrator()
				.setFirstName(administratorViewModel.getFirstName())
				.setLastName(administratorViewModel.getLastName())
				.setAdministratorKey(administratorViewModel.getAdministratorKey());
	}
}
