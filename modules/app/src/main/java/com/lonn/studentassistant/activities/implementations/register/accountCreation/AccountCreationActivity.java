package com.lonn.studentassistant.activities.implementations.register.accountCreation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.activities.implementations.LoginActivity;
import com.lonn.studentassistant.activities.implementations.register.RegistrationInformation;
import com.lonn.studentassistant.databinding.AccountCreationActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.Person;
import com.lonn.studentassistant.firebaselayer.entities.enums.AccountType;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.requests.RegisterRequest;
import com.lonn.studentassistant.firebaselayer.services.abstractions.Service;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.validation.ValidationResult;
import com.lonn.studentassistant.validation.validators.RegistrationValidator;

public abstract class AccountCreationActivity<T extends EntityViewModel<? extends Person>> extends FirebaseConnectedActivity {
	private static final Logger LOGGER = Logger.ofClass(AccountCreationActivity.class);
	RegistrationInformation newAccountCredentials = new RegistrationInformation();
	private T personProfile;
	private AccountType accountType;
	private RegistrationValidator registrationValidator = new RegistrationValidator();
	private String registrationToken;

	public void tapCreateAccountButton(View v) {
		ValidationResult registerValidationResult = registrationValidator.validate(newAccountCredentials);

		if (!registerValidationResult.isValid()) {
			Toast.makeText(this.getBaseContext(),
					registerValidationResult.getErrorMessage(),
					Toast.LENGTH_LONG)
					.show();

			return;
		}

		hideKeyboard();
		showSnackBar("Registering...");

		FirebaseConnection.getInstance(getBaseContext()).execute(new RegisterRequest()
				.email(newAccountCredentials.getEmail())
				.password(newAccountCredentials.getPassword())
				.personUUID(personProfile.getKey())
				.accountType(accountType)
				.onSuccess((user) -> {
					firebaseApi.getAuthenticationService().setAccountType(accountType);
					createOrUpdateProfile(personProfile);
				})
				.onError((exception) -> showSnackBar(exception.getMessage(), 1000)));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		personProfile = (T) getIntent().getSerializableExtra("personProfile");
		accountType = AccountType.valueOf(getIntent().getStringExtra("accountType"));
		registrationToken = getIntent().getStringExtra("registrationToken");
	}

	protected void inflateLayout() {
		AccountCreationActivityLayoutBinding binding =
				DataBindingUtil.setContentView(this, R.layout.account_creation_activity_layout);

		binding.setCredentials(newAccountCredentials);
	}

	private void backToLogin() {
		Intent loginActivityIntent = new Intent(this, LoginActivity.class);
		startActivity(loginActivityIntent);
	}

	private void createOrUpdateProfile(T profile) {
		getPersonService()
				.save(profile)
				.onSuccess(none -> {
					showSnackBar("Account created successfully!", 1000);
					executeWithDelay(this::backToLogin, 2000);
				})
				.onError(error -> {
					firebaseApi.getAuthenticationService()
							.deleteFirebaseAccount();

					logAndShowErrorSnack("An error occurred while setting up your profile",
							error,
							LOGGER);
				});

		if (registrationToken != null) {
			firebaseApi.getRegistrationTokenService().deleteByToken(registrationToken);
		}
	}

	protected abstract Service<?, Exception, T> getPersonService();
}
