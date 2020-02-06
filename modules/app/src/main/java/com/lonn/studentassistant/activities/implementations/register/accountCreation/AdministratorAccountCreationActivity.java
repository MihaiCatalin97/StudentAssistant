package com.lonn.studentassistant.activities.implementations.register.accountCreation;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Administrator;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.abstractions.Service;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.AdministratorViewModel;

public class AdministratorAccountCreationActivity extends AccountCreationActivity<AdministratorViewModel> {

	@Override
	protected Service<Administrator, Exception, AdministratorViewModel> getPersonService() {
		return firebaseApi.getAdministratorService();
	}
}
