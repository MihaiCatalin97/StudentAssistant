package com.lonn.studentassistant.activities.implementations.register.credentialsCheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.activities.implementations.register.profileCreation.AdministratorProfileCreationActivity;
import com.lonn.studentassistant.activities.implementations.register.profileCreation.ProfessorProfileCreationActivity;
import com.lonn.studentassistant.firebaselayer.entities.enums.AccountType;
import com.lonn.studentassistant.logging.Logger;

import static com.lonn.studentassistant.firebaselayer.entities.enums.AccountType.ADMINISTRATOR;
import static com.lonn.studentassistant.firebaselayer.entities.enums.AccountType.PROFESSOR;

public class TokenCheckActivity extends FirebaseConnectedActivity {
	private static final Logger LOGGER = Logger.ofClass(TokenCheckActivity.class);
	private AccountType expectedAccountType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		expectedAccountType = (AccountType) getIntent().getSerializableExtra("accountType");
	}

	protected void inflateLayout() {
		DataBindingUtil.setContentView(this, R.layout.token_check_activity_layout);
	}

	public void checkToken(View view) {
		String token = ((EditText) findViewById(R.id.tokenCheckActivityTokenEditText)).getText().toString();

		firebaseApi.getRegistrationTokenService()
				.getTypeForToken(token)
				.onSuccess(accountType -> {
					if (accountType == null || !accountType.equals(expectedAccountType)) {
						showSnackBar("Invalid registration token", 1000);
					}
					else {
						startNextActivity(token);
					}
				})
				.onError(error -> logAndShowErrorSnack("An error occurred",
						error,
						LOGGER));

	}

	private void startNextActivity(String registrationToken) {
		Intent profileActivityIntent = null;

		if (expectedAccountType.equals(PROFESSOR)) {
			profileActivityIntent = new Intent(this, ProfessorProfileCreationActivity.class);
		}
		else if (expectedAccountType.equals(ADMINISTRATOR)) {
			profileActivityIntent = new Intent(this, AdministratorProfileCreationActivity.class);
		}

		if (profileActivityIntent != null) {
			profileActivityIntent.putExtra("accountType", expectedAccountType);
			profileActivityIntent.putExtra("registrationToken", registrationToken);
			startActivity(profileActivityIntent);
		}
	}
}
