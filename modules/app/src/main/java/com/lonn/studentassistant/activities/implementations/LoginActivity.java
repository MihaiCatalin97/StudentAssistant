package com.lonn.studentassistant.activities.implementations;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.activities.implementations.register.accountChoice.AccountChoiceActivity;
import com.lonn.studentassistant.Utils;
import com.lonn.studentassistant.firebaselayer.requests.LoginRequest;

import java.util.Map;

public class LoginActivity extends FirebaseConnectedActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private CheckBox credentialsRememberCheckBox;
    private AuthenticationSharedPrefs authenticationSharedPrefs;

    public void tapLoginButton(View v) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        boolean rememberCredentials = credentialsRememberCheckBox.isChecked();

        if (email.length() == 0) {
            Toast.makeText(getBaseContext(), "Invalid email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() == 0) {
            Toast.makeText(getBaseContext(), "Invalid password!", Toast.LENGTH_SHORT).show();
            return;
        }

        lockInputs();
        login(email, password, rememberCredentials);
    }

    public void tapSignUpButton(View v) {
        Intent registerActivityIntent = new Intent(this, AccountChoiceActivity.class);
        startActivity(registerActivityIntent);
    }

    public void tapDebugButton(View v) {
        Intent debugIntent = new Intent(this, DebugActivity.class);
        startActivity(debugIntent);
    }

    public void tapRememberCheckBox(View v) {
        if (!((CheckBox) v).isChecked()) {
            authenticationSharedPrefs.deleteCredentials();
        }
    }

    protected void inflateLayout() {
        setContentView(R.layout.login_activity_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        emailEditText = findViewById(R.id.loginEditTextEmail);
        passwordEditText = findViewById(R.id.loginEditTextPassword);
        credentialsRememberCheckBox = findViewById(R.id.loginRememberCheckBox);

        authenticationSharedPrefs = new AuthenticationSharedPrefs(getBaseContext());

        if (authenticationSharedPrefs.hasSavedCredentials()) {
            Map<String, String> storedCredentials = authenticationSharedPrefs.getCredentials();
            String email = storedCredentials.get("email");
            String password = storedCredentials.get("password");
            String rememberCredentialsString = storedCredentials.get("remember");

            boolean rememberCredentials = rememberCredentialsString != null &&
                    rememberCredentialsString.equalsIgnoreCase("true");

            setLoginFields(email, password, rememberCredentials);
        }

        Utils.init(this);
    }

    private void setLoginFields(String email, String password, boolean rememberCredentials) {
        ((EditText) findViewById(R.id.loginEditTextEmail)).setText(email);
        ((EditText) findViewById(R.id.loginEditTextPassword)).setText(password);
        ((CheckBox) findViewById(R.id.loginRememberCheckBox)).setChecked(rememberCredentials);
    }

    private void clearLoginFields() {
        ((EditText) findViewById(R.id.loginEditTextEmail)).setText("");
        ((EditText) findViewById(R.id.loginEditTextPassword)).setText("");
        ((CheckBox) findViewById(R.id.loginRememberCheckBox)).setChecked(false);
    }

    private void login(String email, String password, boolean rememberCredentials) {
        showSnackBar("Logging in...");

        firebaseConnection.execute(new LoginRequest()
                .username(email)
                .password(password)
                .onSuccess(() -> onLoginSuccess(email, password, rememberCredentials))
                .onError(this::onLoginFail));
    }

    private void onLoginSuccess(String email, String password, boolean rememberCredentials) {
        showSnackBar("Login successful!", 650);

        executeWithDelay(() -> {
            unlockInputs();

            if (!rememberCredentials) {
                clearLoginFields();
            }

            startActivity(new Intent(this, StudentActivity.class));
        }, 750);

        if (rememberCredentials) {
            authenticationSharedPrefs.saveCredentials(email, password);
        }
    }

    private void onLoginFail() {
        showSnackBar("Login unsuccessful!", 1000);
        unlockInputs();

        authenticationSharedPrefs.deleteCredentials();
    }

    private void lockInputs() {
        emailEditText.setEnabled(false);
        passwordEditText.setEnabled(false);
        credentialsRememberCheckBox.setEnabled(false);
    }

    private void unlockInputs() {
        emailEditText.setEnabled(true);
        passwordEditText.setEnabled(true);
        credentialsRememberCheckBox.setEnabled(true);
    }
}