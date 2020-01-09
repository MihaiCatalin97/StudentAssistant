package com.lonn.studentassistant.activities.implementations.entityActivities.course;

import android.util.Log;

import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.databinding.CourseEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.Course;

public class CourseEntityActivityFirebaseDispatcher extends Dispatcher<Course> {
	private CourseEntityActivityLayoutBinding binding;

	CourseEntityActivityFirebaseDispatcher(CourseEntityActivity entityActivity) {
		super(entityActivity);
		this.binding = entityActivity.binding;
	}

	void loadAll() {
		firebaseApi.getCourseService()
				.getById(binding.getCourse().getKey())
				.onComplete(course -> {
							binding.setCourse(course);
						},
						error -> {
							Log.e("Loading course", error.getMessage() == null ? "no message" : error.getMessage());
							entityActivity.showSnackBar("An error occurred while loading the course.");
						});
	}
}
