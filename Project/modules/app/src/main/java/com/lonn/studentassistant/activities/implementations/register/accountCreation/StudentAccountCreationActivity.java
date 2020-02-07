package com.lonn.studentassistant.activities.implementations.register.accountCreation;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Student;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.abstractions.Service;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.StudentViewModel;

public class StudentAccountCreationActivity extends AccountCreationActivity<StudentViewModel> {

	@Override
	protected Service<Student, Exception, StudentViewModel> getPersonService() {
		return firebaseApi.getStudentService();
	}
}
