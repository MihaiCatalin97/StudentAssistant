package com.lonn.studentassistant.activities.implementations.entityActivities.professor;

import android.util.Log;

import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.databinding.ProfessorEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.Professor;

class ProfessorEntityActivityFirebaseDispatcher extends Dispatcher<Professor> {
	private ProfessorEntityActivityLayoutBinding binding;

	ProfessorEntityActivityFirebaseDispatcher(ProfessorEntityActivity entityActivity) {
		super(entityActivity);
		this.binding = entityActivity.binding;
	}

	void loadAll() {
		firebaseApi.getProfessorService()
				.getById(binding.getProfessor().getKey())
				.onComplete(binding::setProfessor,
						error -> {
							Log.e("Loading professor", error.getMessage() == null ? "none" : error.getMessage());
							entityActivity.showSnackBar("An error occurred while loading the professor.");
						});
	}
}
