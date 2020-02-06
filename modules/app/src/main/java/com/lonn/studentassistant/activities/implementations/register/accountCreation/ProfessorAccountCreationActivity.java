package com.lonn.studentassistant.activities.implementations.register.accountCreation;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.abstractions.Service;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.ProfessorViewModel;

public class ProfessorAccountCreationActivity extends AccountCreationActivity<ProfessorViewModel> {

	@Override
	protected Service<Professor, Exception, ProfessorViewModel> getPersonService() {
		return firebaseApi.getProfessorService();
	}
}
