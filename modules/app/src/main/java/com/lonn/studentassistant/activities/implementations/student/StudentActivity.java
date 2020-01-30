package com.lonn.studentassistant.activities.implementations.student;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.MainActivity;
import com.lonn.studentassistant.databinding.StudentActivityMainLayoutBinding;
import com.lonn.studentassistant.logging.Logger;

public class StudentActivity extends MainActivity {
	private static final Logger LOGGER = Logger.ofClass(StudentActivity.class);
	StudentActivityMainLayoutBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dispatcher = new StudentActivityFirebaseDispatcher(this);
		dispatcher.loadAll(personId);
	}

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.student_activity_main_layout);
	}
}
