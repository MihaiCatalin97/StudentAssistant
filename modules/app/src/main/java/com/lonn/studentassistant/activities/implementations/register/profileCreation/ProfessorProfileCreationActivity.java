package com.lonn.studentassistant.activities.implementations.register.profileCreation;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.implementations.register.accountCreation.ProfessorAccountCreationActivity;
import com.lonn.studentassistant.activities.implementations.register.accountCreation.StudentAccountCreationActivity;
import com.lonn.studentassistant.databinding.ProfessorProfileCreationActivityLayoutBinding;
import com.lonn.studentassistant.databinding.SingleChoiceListAdapter;
import com.lonn.studentassistant.firebaselayer.api.FirebaseApi;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecializationYear;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.validation.ValidationResult;
import com.lonn.studentassistant.validation.validators.ProfessorValidator;

import static com.lonn.studentassistant.BR._all;
import static com.lonn.studentassistant.firebaselayer.entities.enums.AccountType.PROFESSOR;
import static java.util.UUID.randomUUID;

public class ProfessorProfileCreationActivity extends ProfileCreationActivity<ProfessorViewModel> {
	private static final Logger LOGGER = Logger.ofClass(ProfessorProfileCreationActivity.class);
	private ProfessorProfileCreationActivityLayoutBinding binding;
	private ProfessorValidator professorValidatior = new ProfessorValidator();

	public ProfessorProfileCreationActivity() {
		super(PROFESSOR);
		setPersonProfile(new ProfessorViewModel());
		getPersonProfile().setKey(randomUUID().toString());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		firebaseApi = FirebaseApi.getApi(this);
		inflateLayout();
		binding.setProfessorProfile(getPersonProfile());
	}

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.professor_profile_creation_activity_layout);
	}

	public void tapContinue(View view) {
		ValidationResult validationResult = professorValidatior.validate(getPersonProfile());

		if (!validationResult.isValid()) {
			Toast.makeText(this.getBaseContext(),
					validationResult.getErrorMessage(),
					Toast.LENGTH_LONG)
					.show();

			return;
		}

		startAccountCreationActivity();
	}

	protected ProfessorViewModel mergeExistingProfile(ProfessorViewModel existingProfile,
													ProfessorViewModel newProfile) {
		return existingProfile;
	}

	protected Class<ProfessorAccountCreationActivity> getNextActivityClass(){
		return ProfessorAccountCreationActivity.class;
	}
}