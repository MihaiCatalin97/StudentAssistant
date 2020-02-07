package com.lonn.studentassistant.activities.implementations.register.profileCreation;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.implementations.register.accountCreation.AdministratorAccountCreationActivity;
import com.lonn.studentassistant.databinding.AdministratorProfileCreationActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.AdministratorViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.validation.ValidationResult;
import com.lonn.studentassistant.validation.validators.AdministratorValidator;

import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType.ADMINISTRATOR;
import static java.util.UUID.randomUUID;

public class AdministratorProfileCreationActivity extends ProfileCreationActivity<AdministratorViewModel> {
	private static final Logger LOGGER = Logger.ofClass(ProfessorProfileCreationActivity.class);
	private AdministratorProfileCreationActivityLayoutBinding binding;
	private AdministratorValidator administratorValidator = new AdministratorValidator();

	public AdministratorProfileCreationActivity() {
		super(ADMINISTRATOR);
		setPersonProfile(new AdministratorViewModel());
		getPersonProfile().setKey(randomUUID().toString());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding.setPersonProfile(getPersonProfile());
	}

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.administrator_profile_creation_activity_layout);
	}

	public void tapContinue(View view) {
		ValidationResult validationResult = administratorValidator.validate(getPersonProfile());

		if (!validationResult.isValid()) {
			Toast.makeText(this.getBaseContext(),
					validationResult.getErrorMessage(),
					Toast.LENGTH_LONG)
					.show();

			return;
		}

		startAccountCreationActivity(getPersonProfile());
	}

	protected AdministratorViewModel mergeExistingProfile(AdministratorViewModel existingProfile,
														  AdministratorViewModel newProfile) {
		return existingProfile;
	}

	protected Class<AdministratorAccountCreationActivity> getNextActivityClass() {
		return AdministratorAccountCreationActivity.class;
	}
}