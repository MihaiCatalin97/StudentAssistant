package com.lonn.studentassistant.activities.implementations;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

class AuthenticationSharedPrefs {
    private SharedPreferences sharedPref;

    AuthenticationSharedPrefs(Context context) {
        sharedPref = context.getSharedPreferences("studentassistant-auth",
                Context.MODE_PRIVATE);
    }

    void deleteCredentials() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("remember", false);
        editor.remove("email");
        editor.remove("password");
        editor.apply();
    }

    void saveCredentials(final String email, final String password) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("remember", true);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

    boolean hasSavedCredentials() {
        return sharedPref.getBoolean("remember", false);
    }

    Map<String, String> getCredentials() {
        Map<String, String> credentials = new HashMap<>();

        credentials.put("email",
                sharedPref.getString("email", ""));
        credentials.put("password",
                sharedPref.getString("password", ""));
        credentials.put("remember",
                Boolean.toString(sharedPref.getBoolean("remember", false)));

        return credentials;
    }
}
