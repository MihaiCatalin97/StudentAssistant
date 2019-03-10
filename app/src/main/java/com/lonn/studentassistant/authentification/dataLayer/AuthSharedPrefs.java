package com.lonn.studentassistant.authentification.dataLayer;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AuthSharedPrefs
{
    private SharedPreferences sharedPref;
    private com.lonn.studentassistant.authentification.dataLayer.AuthService authService;

    AuthSharedPrefs(com.lonn.studentassistant.authentification.dataLayer.AuthService authService)
    {
        this.authService = authService;

        sharedPref = authService.getActivity().getBaseContext().getSharedPreferences("studentassistant-auth", Context.MODE_PRIVATE);
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

        authService.getActivity().setLoginFields(new HashMap<String,String>() {{ put("remember", "true"); put("email", email); put("password", password); }});
    }

    public boolean hasSavedCredentials()
    {
        String remember = Boolean.toString(sharedPref.getBoolean("remember", false));

        return remember.equals("true");
    }

    public Map<String,String> getCredentials()
    {
        Map<String, String> creds = new HashMap<>();

        Set<String> keys = sharedPref.getAll().keySet();

        for (String key : keys)
        {
            Object aux = sharedPref.getAll().get("key");

            if (aux instanceof String)
                creds.put(key, (String) aux);
            else if (aux instanceof Boolean)
                creds.put(key, Boolean.toString((boolean) aux));
        }

        return creds;
    }
}
