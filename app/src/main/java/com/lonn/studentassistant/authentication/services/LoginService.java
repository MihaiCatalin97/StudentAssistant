package com.lonn.studentassistant.authentication.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.authentication.AuthSharedPrefs;

public class LoginService extends IntentService
{
    private AuthSharedPrefs authSharedPrefs;

    public LoginService()
    {
        super("Login Service");
        authSharedPrefs = new AuthSharedPrefs();
    }

    @Override
    public void onHandleIntent(Intent intent) {
        final String email = intent.getStringExtra("email");
        final String password = intent.getStringExtra("password");
        final boolean remember = intent.getBooleanExtra("remember", false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Intent intent1 = new Intent("login");

                        if (task.isSuccessful())
                        {
                            if (remember)
                                authSharedPrefs.rememberCredentials(email, password);

                            intent1.putExtra("result", "success");
                        }
                        else {
                            authSharedPrefs.deleteCredentials();
                            intent1.putExtra("result", getResources().getString(R.string.invalid_credentials));
                        }

                        intent1.putExtra("email", email);
                        intent1.putExtra("password", password);
                        intent1.putExtra("remember", remember);

                        sendBroadcast(intent1);
                    }
                });
    }
}
