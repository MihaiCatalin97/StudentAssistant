package com.lonn.studentassistant.authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

        Log.e("Delete", "true");
    }

    public void rememberCredentials(final String email, final String password)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("remember", true);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();

        Log.e("Remember", "true");
    }

    boolean hasSavedCredentials()
    {
        boolean remember = sharedPref.getBoolean("remember", false);

        Log.e("Saved", Boolean.toString(remember));
        Log.e("Count", Integer.toString(sharedPref.getAll().size()));

        return remember;
    }

    Map<String,String> getCredentials()
    {
        Map<String, String> creds = new HashMap<>();

        Set<String> keys = sharedPref.getAll().keySet();

        creds.put("email", sharedPref.getString("email",""));
        creds.put("password", sharedPref.getString("password",""));
        creds.put("remember", Boolean.toString(sharedPref.getBoolean("remember",false)));

        return creds;
    }
}
