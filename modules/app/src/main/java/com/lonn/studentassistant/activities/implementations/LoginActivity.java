package com.lonn.studentassistant.activities.implementations;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.activities.implementations.administrator.AdministratorActivity;
import com.lonn.studentassistant.activities.implementations.professor.ProfessorActivity;
import com.lonn.studentassistant.activities.implementations.register.accountChoice.AccountChoiceActivity;
import com.lonn.studentassistant.activities.implementations.student.StudentActivity;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.utils.Utils;

import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

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
			makeText(getBaseContext(), "Invalid email!", LENGTH_SHORT).show();
			return;
		}
		if (password.length() == 0) {
			makeText(getBaseContext(), "Invalid password!", LENGTH_SHORT).show();
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
		//TODO: Investigate persistance
//		FirebaseDatabase.getInstance().setPersistenceEnabled(true);

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
		showSnackBar("Login successful!", 500);

		executeWithDelay(() -> {
			unlockInputs();

			if (!rememberCredentials) {
				clearLoginFields();
			}

			startNextActivity();
		}, 1000);

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
		if (FirebaseAuth.getInstance().getCurrentUser() == null) {
			return;
		}

		String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

		firebaseApi.getUserService()
				.getById(uid, true)
				.onSuccess(user -> {
					switch (user.getAccountType()) {
						case STUDENT: {
							startActivity(StudentActivity.class, user.getPersonUUID());
							break;
						}
						case PROFESSOR: {
							startActivity(ProfessorActivity.class, user.getPersonUUID());
							break;
						}
						case ADMINISTRATOR: {
							startActivity(AdministratorActivity.class, user.getPersonUUID());
							break;
						}
						default: {
							logAndShowErrorSnack("Unknown account type",
									new Exception("Unknown account type `" + user.getAccountType() + "`"),
									LOGGER);
							break;
						}
					}
				}).onError(error -> logAndShowErrorSnack("An error occurred while logging in!",
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