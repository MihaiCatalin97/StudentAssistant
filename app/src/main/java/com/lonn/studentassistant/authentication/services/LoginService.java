package com.lonn.studentassistant.authentication.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.authentication.AuthSharedPrefs;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.entities.User;

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

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(Utils.emailToKey(email));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);

                if (user != null)
                {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                    {
                                        if (remember)
                                            authSharedPrefs.rememberCredentials(email, password);

                                        sendBroadcastResult("success", email, password, remember, user.getPrivileges());
                                    }
                                    else {
                                        authSharedPrefs.deleteCredentials();

                                        sendBroadcastResult(
                                                getResources().getString(R.string.invalid_credentials),
                                                null, null, false, null);
                                    }
                                }
                            });
                }
                else
                    sendBroadcastResult(
                            getResources().getString(R.string.invalid_credentials),
                            null, null, false, null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void sendBroadcastResult(String result, String email, String password, boolean remember, String accountType)
    {
        Intent intent1 = new Intent("login");

        intent1.putExtra("result", result);
        intent1.putExtra("email", email);
        intent1.putExtra("password", password);
        intent1.putExtra("remember", remember);
        intent1.putExtra("accountType", accountType);

        sendBroadcast(intent1);
    }
}
