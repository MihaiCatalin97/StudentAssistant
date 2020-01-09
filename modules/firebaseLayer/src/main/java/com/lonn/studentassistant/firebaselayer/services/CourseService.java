package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.CourseAdapter;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.COURSES;

public class CourseService extends Service<Course, Exception, CourseViewModel> {
	private static CourseService instance;

	public static CourseService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new CourseService(firebaseConnection);
		}

		return instance;
	}

	private CourseService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
		adapter = new CourseAdapter();
	}

	@Override
	protected DatabaseTable<Course> getDatabaseTable() {
		return COURSES;
	}
}
