package com.lonn.studentassistant.activities.implementations.entityActivities.laboratory;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.databinding.LaboratoryInputActivityLayoutBinding;
import com.lonn.studentassistant.logging.Logger;

public class LaboratoryInputActivity extends FirebaseConnectedActivity {
	private static final Logger LOGGER = Logger.ofClass(LaboratoryInputActivity.class);
	LaboratoryInputActivityLayoutBinding binding;
	private LaboratoryInputActivityFirebaseDispatcher dispatcher;

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.laboratory_input_activity_layout);
		dispatcher = new LaboratoryInputActivityFirebaseDispatcher(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding.setCourseName(getIntent().getStringExtra("courseName"));
	}
}
