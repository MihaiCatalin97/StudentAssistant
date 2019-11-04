package com.lonn.studentassistant.activities.implementations.register.accountChoice;

import android.content.Intent;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.activities.implementations.register.credentialsCheck.AdministratorCheckActivity;
import com.lonn.studentassistant.activities.implementations.register.credentialsCheck.ProfessorCheckActivity;
import com.lonn.studentassistant.activities.implementations.register.credentialsCheck.StudentCheckActivity;

public class AccountChoiceActivity extends FirebaseConnectedActivity {
    public void tapRegistrationAccountTypeButton(View v) {
        Intent credentialsCheckActivityIntent = null;

        switch (v.getId()) {
            case R.id.buttonRegisterStudent: {
                credentialsCheckActivityIntent = new Intent(this, StudentCheckActivity.class);
                break;
            }
            case R.id.buttonRegisterProfessor: {
                credentialsCheckActivityIntent = new Intent(this, ProfessorCheckActivity.class);
                break;
            }
            case R.id.buttonRegisterAdministrator: {
                credentialsCheckActivityIntent = new Intent(this, AdministratorCheckActivity.class);
                break;
            }
        }

        if (credentialsCheckActivityIntent != null) {
            startActivity(credentialsCheckActivityIntent);
        }
    }

    protected void inflateLayout() {
        DataBindingUtil.setContentView(this, R.layout.accout_choice_activity_layout);
    }
}
