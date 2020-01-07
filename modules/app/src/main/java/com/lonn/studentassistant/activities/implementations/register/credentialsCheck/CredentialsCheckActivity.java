package com.lonn.studentassistant.activities.implementations.register.credentialsCheck;

import android.content.Intent;
import android.view.View;

import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.activities.implementations.LoginActivity;
import com.lonn.studentassistant.activities.implementations.register.AccountCreationActivity;
import com.lonn.studentassistant.firebaselayer.requests.CredentialsCheckRequest;

abstract class CredentialsCheckActivity extends FirebaseConnectedActivity {
    public void tapCredentialsCheckButton(View v) {
        checkIdentificationHash();
    }

    protected abstract String getCredentialsHash();

    private void checkIdentificationHash() {
        hideKeyboard();

        String credentialsHash = getCredentialsHash();
        showSnackBar("Checking credentials");

        firebaseConnection.execute(new CredentialsCheckRequest()
                .identificationHash(credentialsHash)
                .onSuccess((receivedIdentification) -> {
                    showSnackBar("You have been successfully identified!", 1000);
                    executeWithDelay(() ->
                                    startAccountCreationActivity(receivedIdentification.getEntityKey()),
                            1500);
                })
                .onError((errorMessage) -> {
                    showSnackBar(errorMessage, 1000);
                }));
    }

    private void startAccountCreationActivity(String personKey) {
        Intent accountCreationActivityIntent =
                new Intent(this, AccountCreationActivity.class);

        accountCreationActivityIntent.putExtra("personKey", personKey);
        this.startActivity(accountCreationActivityIntent);
    }

    public void backToLogin(View view) {
        Intent loginActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(loginActivityIntent);
    }
}
