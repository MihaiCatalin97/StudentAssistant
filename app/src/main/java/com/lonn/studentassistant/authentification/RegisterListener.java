package com.lonn.studentassistant.authentification;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.lonn.studentassistant.entities.User;

public class RegisterListener implements OnCompleteListener<AuthResult>
{
    private com.lonn.studentassistant.authentification.dataLayer.AuthService authService;
    private com.lonn.studentassistant.authentification.dataLayer.AuthSharedPrefs authSharedPrefs;
    private String email, password;

    public RegisterListener(com.lonn.studentassistant.authentification.dataLayer.AuthService authService,
                     com.lonn.studentassistant.authentification.dataLayer.AuthSharedPrefs authSharedPrefs,
                     String email,
                     String password)
    {
        this.authService = authService;
        this.authSharedPrefs = authSharedPrefs;
        this.email = email;
        this.password = password;
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information

            Toast.makeText(authService.getActivity(), "Registration success.",
                    Toast.LENGTH_SHORT).show();
            authService.getActivity().showLoginLayout(null);
            authService.getUserRepository().add(new User(email, password));
            authSharedPrefs.rememberCredentials(email, password);

            //updateUI(user);
        } else {
            // If sign in fails, display a message to the user.
            Toast.makeText(authService.getActivity(), "Registration failed.",
                    Toast.LENGTH_SHORT).show();
            //updateUI(null);
        }
    }
}
