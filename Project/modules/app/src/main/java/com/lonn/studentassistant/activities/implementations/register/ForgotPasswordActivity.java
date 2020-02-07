package com.lonn.studentassistant.activities.implementations.register;

import android.view.View;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.logging.Logger;

import static com.lonn.studentassistant.validation.Regex.EMAIL_REGEX;

public class ForgotPasswordActivity extends FirebaseConnectedActivity {
	private static Logger logger;

	protected void inflateLayout() {
		logger = Logger.ofClass(ForgotPasswordActivity.class);
		DataBindingUtil.setContentView(this, R.layout.forgot_password_activity_layout);
	}

	public void resetPassword(View view) {
		String email = ((TextView) findViewById(R.id.forgotPasswordEditTextEmail)).getText().toString();

		if (!email.matches(EMAIL_REGEX)) {
			showSnackBar("Invalid email!", 1500);
		}
		else {
			FirebaseAuth.getInstance().sendPasswordResetEmail(email)
					.addOnCompleteListener(task -> {
						if (task.isSuccessful()) {
							showSnackBar("An email was sent to " + email, 2000);
						}
						else {
							logAndShowErrorSnack("An error occurred!",
									task.getException(),
									logger);
						}
					});
		}
	}
}
