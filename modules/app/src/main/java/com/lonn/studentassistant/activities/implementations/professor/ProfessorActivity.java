package com.lonn.studentassistant.activities.implementations.professor;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.MainActivity;
import com.lonn.studentassistant.databinding.ProfessorActivityMainLayoutBinding;
import com.lonn.studentassistant.logging.Logger;

public class ProfessorActivity extends MainActivity {
	private static final Logger LOGGER = Logger.ofClass(ProfessorActivity.class);
	ProfessorActivityMainLayoutBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dispatcher = new ProfessorActivityFirebaseDispatcher(this);
		dispatcher.loadAll(personId);
	}

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.professor_activity_main_layout);
	}
}
