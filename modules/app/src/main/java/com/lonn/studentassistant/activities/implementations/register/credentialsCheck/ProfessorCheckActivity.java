package com.lonn.studentassistant.activities.implementations.register.credentialsCheck;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.databinding.ProfessorCheckActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.IdentificationHash;
import com.lonn.studentassistant.viewModels.authentication.ProfessorCredentials;

public class ProfessorCheckActivity extends CredentialsCheckActivity {
	private ProfessorCredentials professorCredentials = new ProfessorCredentials();

	protected void inflateLayout() {
		ProfessorCheckActivityLayoutBinding binding =
				DataBindingUtil.setContentView(this, R.layout.professor_check_activity_layout);

		binding.setCredentials(professorCredentials);
	}

	protected String getCredentialsHash() {
		return IdentificationHash.of(professorCredentials.toProfessor())
				.getHash();
	}
}
