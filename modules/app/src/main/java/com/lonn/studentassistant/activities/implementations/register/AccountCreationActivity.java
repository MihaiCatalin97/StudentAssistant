package com.lonn.studentassistant.activities.implementations.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.activities.implementations.LoginActivity;
import com.lonn.studentassistant.databinding.AccountCreationActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.enums.AccountType;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.requests.RegisterRequest;
import com.lonn.studentassistant.validation.ValidationResult;
import com.lonn.studentassistant.validation.validators.RegistrationValidator;

import static com.lonn.studentassistant.firebaselayer.entities.enums.AccountType.STUDENT;

public class AccountCreationActivity extends FirebaseConnectedActivity {
	RegistrationInformation newAccountCredentials = new RegistrationInformation();
	private String personUUID;
	private AccountType accountType;
	private RegistrationValidator registrationValidator = new RegistrationValidator();

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
				.personUUID(personUUID)
				.accountType(accountType)
				.onSuccess((user) -> {
					showSnackBar("Account created successfully!", 1000);
					executeWithDelay(this::backToLogin, 1500);
				})
				.onError((exception) -> showSnackBar(exception.getMessage(), 1000)));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		personUUID = getIntent().getStringExtra("personUUID");
		accountType = AccountType.valueOf(getIntent().getStringExtra("accountType"));
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
}
