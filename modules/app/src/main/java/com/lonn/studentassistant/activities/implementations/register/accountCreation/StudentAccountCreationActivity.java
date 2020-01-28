package com.lonn.studentassistant.activities.implementations.register.accountCreation;

import com.lonn.studentassistant.firebaselayer.entities.Student;
import com.lonn.studentassistant.firebaselayer.services.abstractions.Service;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;

public class StudentAccountCreationActivity extends AccountCreationActivity<StudentViewModel> {

	@Override
	protected Service<Student, Exception, StudentViewModel> getPersonService() {
		return firebaseApi.getStudentService();
	}
}
