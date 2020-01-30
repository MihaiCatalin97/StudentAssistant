package com.lonn.studentassistant.activities.implementations.administrator;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.MainActivity;
import com.lonn.studentassistant.databinding.AdministratorActivityMainLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.enums.AccountType;
import com.lonn.studentassistant.logging.Logger;

import static com.lonn.studentassistant.firebaselayer.entities.enums.AccountType.ADMINISTRATOR;
import static com.lonn.studentassistant.firebaselayer.entities.enums.AccountType.PROFESSOR;
import static com.lonn.studentassistant.validation.predicates.StringValidationPredicates.isValidEmail;

public class AdministratorActivity extends MainActivity {
	private static final Logger LOGGER = Logger.ofClass(AdministratorActivity.class);
	AdministratorActivityMainLayoutBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dispatcher = new AdministratorActivityFirebaseDispatcher(this);
		dispatcher.loadAll(personId);
	}

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.administrator_activity_main_layout);
	}

	public void createRegistrationToken(View view) {
		AccountType accountType = getSelectedAccountType();
		String email = ((EditText) findViewById(R.id.registrationTokenEmailEditText)).getText().toString();

		if (!isValidEmail(email)) {
			showSnackBar("Invalid email", 1000);
			return;
		}

		showSnackBar("Creating and sending token to " + email);
		firebaseApi.getRegistrationTokenService()
				.createTokenAndSendEmail(accountType, email)
				.onSuccess(none -> {
					showSnackBar("The token has been created and send to " + email, 1000);
					((EditText) findViewById(R.id.registrationTokenEmailEditText)).setText("");
				})
				.onError(error -> logAndShowErrorSnack("An error occurred!",
						error,
						LOGGER));
	}

	private AccountType getSelectedAccountType() {
		if (((RadioButton) findViewById(R.id.registrationTokenProfessorRadioButton)).isChecked()) {
			return PROFESSOR;
		}

		return ADMINISTRATOR;
	}
}
