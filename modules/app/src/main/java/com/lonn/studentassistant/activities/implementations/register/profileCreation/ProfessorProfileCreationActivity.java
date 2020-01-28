package com.lonn.studentassistant.activities.implementations.register.profileCreation;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;

public class ProfessorProfileCreationActivity extends FirebaseConnectedActivity {
	protected void inflateLayout() {
		DataBindingUtil.setContentView(this, R.layout.professor_profile_creation_activity_layout);
	}
}
