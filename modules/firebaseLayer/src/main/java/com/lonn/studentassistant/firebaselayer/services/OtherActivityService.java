package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.OtherActivityAdapter;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.services.abstractions.DisciplineService;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.OTHER_ACTIVITIES;

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

	protected void unlinkToStudent(StudentViewModel student, String disciplineKey) {
		student.getOtherActivities().remove(disciplineKey);

		studentService.save(student);
	}

	protected void unlinkToProfessor(ProfessorViewModel professor, String disciplineKey) {
		professor.getOtherActivities().remove(disciplineKey);

		professorService.save(professor);
	}
}
