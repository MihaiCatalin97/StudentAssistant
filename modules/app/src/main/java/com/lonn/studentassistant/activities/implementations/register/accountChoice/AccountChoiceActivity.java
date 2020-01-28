package com.lonn.studentassistant.activities.implementations.register.accountChoice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.activities.implementations.register.credentialsCheck.TokenCheckActivity;
import com.lonn.studentassistant.activities.implementations.register.profileCreation.StudentProfileCreationActivity;
import com.lonn.studentassistant.firebaselayer.api.FirebaseApi;

public class AccountChoiceActivity extends FirebaseConnectedActivity {
	public void tapRegistrationAccountTypeButton(View v) {
		Intent nextActivityIntent = null;

		switch (v.getId()) {
			case R.id.buttonRegisterStudent: {
				nextActivityIntent = new Intent(this, StudentProfileCreationActivity.class);
				break;
			}
			case R.id.buttonRegisterAdministrator:
			case R.id.buttonRegisterProfessor: {
				nextActivityIntent = new Intent(this, TokenCheckActivity.class);
				break;
			}
		}

		if (nextActivityIntent != null) {
			startActivity(nextActivityIntent);
		}
	}

	protected void inflateLayout() {
		setContentView(R.layout.accout_choice_activity_layout);
	}

	public void backToLogin(View view) {
		super.onBackPressed();
	}
}
