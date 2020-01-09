package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.StudentAdapter;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.Student;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.STUDENTS;

public class StudentService extends Service<Student, Exception, StudentViewModel> {
	private static StudentService instance;

	public static StudentService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new StudentService(firebaseConnection);
		}

		return instance;
	}

	private StudentService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
		adapter = new StudentAdapter();
	}

	@Override
	protected DatabaseTable<Student> getDatabaseTable() {
		return STUDENTS;
	}
}
