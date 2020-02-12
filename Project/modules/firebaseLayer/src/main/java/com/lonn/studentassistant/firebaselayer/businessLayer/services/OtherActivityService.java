package com.lonn.studentassistant.firebaselayer.businessLayer.services;

import com.lonn.studentassistant.firebaselayer.businessLayer.adapters.OtherActivityAdapter;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.PermissionLevel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.abstractions.DisciplineService;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.StudentViewModel;

import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.DatabaseTableContainer.OTHER_ACTIVITIES;

public class OtherActivityService extends DisciplineService<OtherActivity, OtherActivityViewModel> {
	private static OtherActivityService instance;

	private OtherActivityService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
	}

	public static OtherActivityService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new OtherActivityService(firebaseConnection);
			instance.init();
		}

		return instance;
	}

	protected void init() {
		super.init();
		adapter = new OtherActivityAdapter();
	}

	@Override
	protected DatabaseTable<OtherActivity> getDatabaseTable() {
		return OTHER_ACTIVITIES;
	}

	protected void linkToStudent(String studentKey, String disciplineKey) {
		studentService.addActivity(studentKey, disciplineKey);
	}

	protected void linkToProfessor(String professorKey, String disciplineKey) {
		professorService.addActivity(professorKey, disciplineKey);
	}

	protected void unlinkToStudent(String studentKey, String disciplineKey) {
		studentService.removeActivity(studentKey, disciplineKey);
	}

	protected void unlinkToStudent(StudentViewModel student, String disciplineKey) {
		student.getOtherActivities().remove(disciplineKey);

		studentService.save(student);
	}

	protected void unlinkToProfessor(ProfessorViewModel professor, String disciplineKey) {
		professor.getOtherActivities().remove(disciplineKey);

		professorService.save(professor);
	}

	protected void unlinkToProfessor(String professorKey, String disciplineKey) {
		professorService.removeActivity(professorKey, disciplineKey);
	}

	protected PermissionLevel getPermissionLevel(OtherActivity activity) {
		return authenticationService.getPermissionLevel(activity);
	}
}
