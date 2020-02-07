package com.lonn.studentassistant.firebaselayer.dataAccessLayer.firebaseConnection;

import android.util.Log;

public class RequestLogger {
	public void logLoginSuccess(String userName) {
		Log.i("Login", "Login successful for user " + userName);
	}

	public void logLoginFail(String userName, String errorMessage) {
		Log.e("Login", "Login error for user " + userName + ": \n" + errorMessage);
	}

	public void logRegisterSuccess(String email, String uid) {
		Log.i("Registration", "User with email " + email
				+ " and UID " + uid
				+ " created!");
	}

	public void logRegisterFail(String email, String errorMessage) {
		Log.e("Registration", "Failed creating user with email " + email
				+ ":\n" + errorMessage);
	}

	public void logRegistrationLinkingSuccess(String uid, String identificationHash) {
		Log.i("Registration Linking", "User with UID " + uid +
				" linked to " + identificationHash);
	}

	public void logRegistrationLinkingFail(String uid, String identificationHash, String errorMessage) {
		Log.e("Registration Linking", "Failed to link user with UID "
				+ uid + " to identification hash "
				+ identificationHash + ":\n"
				+ errorMessage);
	}

	public void logCredentialsCheckSuccess(String identificationHash) {
		Log.i("Credentials Check", "Successfully checked credentials for " +
				"identification hash " + identificationHash);
	}

	public void logCredentialsCheckFail(String identificationHash, String errorMessage) {
		Log.e("Credentials Check", "Failed checking credentials for " +
				"identification hash " + identificationHash + ": \n" + errorMessage);
	}
}
