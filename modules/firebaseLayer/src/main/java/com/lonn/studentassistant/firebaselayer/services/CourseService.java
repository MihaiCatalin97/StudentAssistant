package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.api.tasks.FirebaseListTask;
import com.lonn.studentassistant.firebaselayer.api.tasks.FirebaseSingleTask;
import com.lonn.studentassistant.firebaselayer.api.tasks.FirebaseTask;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;

import java.util.List;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.COURSES;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.BaseEntityField.ID;

public class CourseService {
	private FirebaseConnection firebaseConnection;

	CourseService(FirebaseConnection firebaseConnection) {
		this.firebaseConnection = firebaseConnection;
	}

	public FirebaseTask<List<Course>, Exception> getAll() {
		FirebaseTask<List<CourseViewModel>, Exception> task = new FirebaseListTask<>(firebaseConnection,
				new GetRequest<Course>()
				.databaseTable(COURSES));
	}

	public FirebaseTask<Course, Exception> getById(String id) {
		return new FirebaseSingleTask<>(firebaseConnection, new GetRequest<Course>()
				.databaseTable(COURSES)
				.predicate(where(ID).equalTo(id)));
	}
}
