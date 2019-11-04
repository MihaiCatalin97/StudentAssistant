package com.lonn.studentassistant.firebaselayer.firebaseConnection;

import android.util.Log;

class RequestLogger {
    void logLoginSuccess(String userName) {
        Log.i("Login", "Login successful for user " + userName);
    }

    void logLoginFail(String userName, String errorMessage) {
        Log.e("Login", "Login error for user " + userName + ": \n" + errorMessage);
    }

    void logRegisterSuccess(String email, String uid) {
        Log.i("Registration", "User with email " + email
                + " and UID " + uid
                + " created!");
    }

    void logRegisterFail(String email, String errorMessage) {
        Log.e("Registration", "Failed creating user with email " + email
                + ":\n" + errorMessage);
    }

    void logRegistrationLinkingSuccess(String uid, String identificationHash) {
        Log.i("Registration Linking", "User with UID " + uid +
                " linked to " + identificationHash);
    }

    void logRegistrationLinkingFail(String uid, String identificationHash, String errorMessage) {
        Log.e("Registration Linking", "Failed to link user with UID "
                + uid + " to identification hash "
                + identificationHash + ":\n"
                + errorMessage);
    }

    void logCredentialsCheckSuccess(String identificationHash) {
        Log.i("Credentials Check", "Successfully checked credentials for " +
                "identification hash " + identificationHash);
    }

    void logCredentialsCheckFail(String identificationHash, String errorMessage) {
        Log.e("Credentials Check", "Failed checking credentials for " +
                "identification hash " + identificationHash + ": \n" + errorMessage);
    }
}
