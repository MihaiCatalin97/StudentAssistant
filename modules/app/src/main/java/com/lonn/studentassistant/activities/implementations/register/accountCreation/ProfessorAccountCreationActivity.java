package com.lonn.studentassistant.activities.implementations.register.accountCreation;

import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.services.abstractions.Service;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;

public class ProfessorAccountCreationActivity extends AccountCreationActivity<ProfessorViewModel> {

	@Override
	protected Service<Professor, Exception, ProfessorViewModel> getPersonService() {
		return firebaseApi.getProfessorService();
	}
}
