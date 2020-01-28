package com.lonn.studentassistant.activities.implementations.register.accountCreation;

import com.lonn.studentassistant.firebaselayer.entities.Administrator;
import com.lonn.studentassistant.firebaselayer.services.abstractions.Service;
import com.lonn.studentassistant.firebaselayer.viewModels.AdministratorViewModel;

public class AdministratorAccountCreationActivity extends AccountCreationActivity<AdministratorViewModel> {

	@Override
	protected Service<Administrator, Exception, AdministratorViewModel> getPersonService() {
		return firebaseApi.getAdministratorService();
	}
}
