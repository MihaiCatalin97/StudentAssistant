package com.lonn.studentassistant.activities.implementations.register.credentialsCheck;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.databinding.StudentCheckActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.IdentificationHash;
import com.lonn.studentassistant.viewModels.authentication.StudentCredentials;

public class StudentCheckActivity extends CredentialsCheckActivity {
	private StudentCredentials studentCredentials = new StudentCredentials();

	protected void inflateLayout() {
		StudentCheckActivityLayoutBinding binding =
				DataBindingUtil.setContentView(this, R.layout.student_check_activity_layout);

		binding.setCredentials(studentCredentials);
	}

	protected String getCredentialsHash() {
		return IdentificationHash.of(studentCredentials.toStudent())
				.getHash();
	}
}
