package com.lonn.studentassistant.activities.implementations.entityActivities.laboratory;

import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.databinding.LaboratoryInputActivityLayoutBinding;

public class LaboratoryInputActivityFirebaseDispatcher extends Dispatcher {
	LaboratoryInputActivityLayoutBinding binding;

	LaboratoryInputActivityFirebaseDispatcher(LaboratoryInputActivity activity) {
		super(activity);
		this.binding = activity.binding;
	}

	void loadAll(String courseKey) {
		firebaseApi.getCourseService()
				.getById(courseKey)
				.onComplete(course -> {
					binding.setCourseName(course.name);
				});
	}
}
