package com.lonn.studentassistant.activities.implementations.authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class AuthSharedPrefs
{
    private static SharedPreferences sharedPref;

    static void init(Context context)
    {
        sharedPref = context.getSharedPreferences("studentassistant-auth", Context.MODE_PRIVATE);
    }

    public void deleteCredentials()
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("remember", false);
        editor.remove("email");
        editor.remove("password");
        editor.apply();
    }

    public void rememberCredentials(final String email, final String password)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("remember", true);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

    boolean hasSavedCredentials()
    {
        return sharedPref.getBoolean("remember", false);
    }

    Map<String,String> getCredentials()
    {
        Map<String, String> creds = new HashMap<>();

        creds.put("email", sharedPref.getString("email",""));
        creds.put("password", sharedPref.getString("password",""));
        creds.put("remember", Boolean.toString(sharedPref.getBoolean("remember",false)));

        return creds;
    }
}
