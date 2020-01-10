package com.lonn.studentassistant.activities.implementations;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.activities.implementations.register.accountChoice.AccountChoiceActivity;
import com.lonn.studentassistant.activities.implementations.student.StudentActivity;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.utils.Utils;

import java.util.Map;

public class LoginActivity extends FirebaseConnectedActivity {
	private static final Logger LOGGER = Logger.ofClass(LoginActivity.class);
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

	protected void inflateLayout() {
		setContentView(R.layout.login_activity_layout);
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

		firebaseApi.getAuthenticationService()
				.login(email, password)
				.onComplete(none -> onLoginSuccess(email, password, rememberCredentials),
						exception -> onLoginFail());
	}

	private void onLoginSuccess(String email, String password, boolean rememberCredentials) {
		showSnackBar("Login successful!", 650);

		executeWithDelay(() -> {
			unlockInputs();

			if (!rememberCredentials) {
				clearLoginFields();
			}

			startNextActivity();
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

	private void startNextActivity() {
		if (FirebaseAuth.getInstance().getCurrentUser() == null)
			return;

		String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
		showSnackBar("Loading your personal data...");

		firebaseApi.getUserService()
				.getById(uid)
				.onComplete(user -> {
							switch (String.valueOf(user.accountType).toLowerCase()) {
								case "student": {
									startActivity(StudentActivity.class, user.personUUID);
									break;
								}
								case "professor": {
									// TODO: Start professor activity
									break;
								}
								case "administrator": {
									// TODO: Start administrator activity
									break;
								}
								default: {
									logAndShowError("Unknown account type",
											new Exception("Unknown account type `" + user.accountType + "`"),
											LOGGER);
									break;
								}
							}
						},
						error -> logAndShowError("An error occurred while logging in!",
								new Exception("Unknown account"),
								LOGGER));
	}

	private void startActivity(Class<? extends FirebaseConnectedActivity> activityClass,
							   String personId) {
		Intent activityIntent = new Intent(this, activityClass);

		activityIntent.putExtra("personId", personId);

		startActivity(activityIntent);
	}
}