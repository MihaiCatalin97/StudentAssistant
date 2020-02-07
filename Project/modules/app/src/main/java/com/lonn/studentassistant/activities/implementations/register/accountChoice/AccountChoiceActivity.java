package com.lonn.studentassistant.activities.implementations.register.accountChoice;

import android.content.Intent;
import android.view.View;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.activities.implementations.register.credentialsCheck.TokenCheckActivity;
import com.lonn.studentassistant.activities.implementations.register.profileCreation.StudentProfileCreationActivity;

import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType.ADMINISTRATOR;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType.PROFESSOR;

public class AccountChoiceActivity extends FirebaseConnectedActivity {
	public void tapRegistrationAccountTypeButton(View v) {
		Intent nextActivityIntent = null;

		switch (v.getId()) {
			case R.id.buttonRegisterStudent: {
				nextActivityIntent = new Intent(this, StudentProfileCreationActivity.class);
				break;
			}
			case R.id.buttonRegisterAdministrator: {
				nextActivityIntent = new Intent(this, TokenCheckActivity.class);
				nextActivityIntent.putExtra("accountType", ADMINISTRATOR);
				break;
			}
			case R.id.buttonRegisterProfessor: {
				nextActivityIntent = new Intent(this, TokenCheckActivity.class);
				nextActivityIntent.putExtra("accountType", PROFESSOR);
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
