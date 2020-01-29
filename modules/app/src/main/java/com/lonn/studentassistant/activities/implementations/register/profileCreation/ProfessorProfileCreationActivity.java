package com.lonn.studentassistant.activities.implementations.register.profileCreation;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.implementations.register.accountCreation.ProfessorAccountCreationActivity;
import com.lonn.studentassistant.databinding.ProfessorProfileCreationActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.validation.ValidationResult;
import com.lonn.studentassistant.validation.validators.ProfessorValidator;

import static com.lonn.studentassistant.firebaselayer.entities.enums.AccountType.PROFESSOR;
import static java.util.UUID.randomUUID;

public class ProfessorProfileCreationActivity extends ProfileCreationActivity<ProfessorViewModel> {
	private static final Logger LOGGER = Logger.ofClass(ProfessorProfileCreationActivity.class);
	private ProfessorProfileCreationActivityLayoutBinding binding;
	private ProfessorValidator professorValidator = new ProfessorValidator();

	public ProfessorProfileCreationActivity() {
		super(PROFESSOR);
		setPersonProfile(new ProfessorViewModel());
		getPersonProfile().setKey(randomUUID().toString());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding.setProfessorProfile(getPersonProfile());
	}

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.professor_profile_creation_activity_layout);
	}

	public void tapContinue(View view) {
		ValidationResult validationResult = professorValidator.validate(getPersonProfile());

		if (!validationResult.isValid()) {
			Toast.makeText(this.getBaseContext(),
					validationResult.getErrorMessage(),
					Toast.LENGTH_LONG)
					.show();

			return;
		}

		firebaseApi.getProfessorService()
				.getByName(getPersonProfile().getFirstName(), getPersonProfile().getLastName())
				.onSuccess(professor -> {
					if (professor != null) {
						checkIfPersonHasAccountAndStartActivity(professor);
					}
					else {
						startAccountCreationActivity(getPersonProfile());
					}
				})
				.onError(error -> logAndShowErrorSnack("An error occurred, please try again later",
						error,
						LOGGER));
		startAccountCreationActivity(getPersonProfile());
	}

	protected ProfessorViewModel mergeExistingProfile(ProfessorViewModel existingProfile,
													  ProfessorViewModel newProfile) {
		existingProfile.setFirstName(newProfile.getFirstName())
				.setLastName(newProfile.getLastName());

		if (newProfile.getEmail() != null && newProfile.getEmail().length() > 0) {
			existingProfile.setEmail(newProfile.getEmail());
		}
		if (newProfile.getRank() != null && newProfile.getRank().length() > 0) {
			existingProfile.setRank(newProfile.getRank());
		}
		if (newProfile.getPhoneNumber() != null && newProfile.getPhoneNumber().length() > 0) {
			existingProfile.setPhoneNumber(newProfile.getPhoneNumber());
		}

		return existingProfile;
	}

	protected Class<ProfessorAccountCreationActivity> getNextActivityClass() {
		return ProfessorAccountCreationActivity.class;
	}
}