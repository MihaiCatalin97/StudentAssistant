package com.lonn.studentassistant.authentification.dataLayer;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.authentification.AuthentificationActivity;
import com.lonn.studentassistant.authentification.LoginListener;
import com.lonn.studentassistant.authentification.RegisterListener;
import com.lonn.studentassistant.entities.Student;
import com.lonn.studentassistant.firebaseDatabase.students.StudentDatabaseController;
import com.lonn.studentassistant.firebaseDatabase.students.StudentRepository;
import com.lonn.studentassistant.firebaseDatabase.users.UserDatabaseController;
import com.lonn.studentassistant.firebaseDatabase.users.UserRepository;

public class AuthService
{
    private UserRepository userRepository;
    private AuthSharedPrefs sharedPreferences;
    private StudentRepository studentRepository;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private AuthentificationActivity activity;

    public AuthentificationActivity getActivity() {
        return activity;
    }

    public AuthSharedPrefs getSharedPreferences() {
        return sharedPreferences;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public AuthService(AuthentificationActivity activity)
    {
        mAuth = FirebaseAuth.getInstance();
        this.activity = activity;

        userRepository = new UserRepository(new UserDatabaseController());
        studentRepository = new StudentRepository(new StudentDatabaseController());
        sharedPreferences = new AuthSharedPrefs(this);
    }

    private FirebaseUser getUser()
    {
        return mAuth.getCurrentUser();
    }

    public void notifyUserChanged()
    {
        this.currentUser = getUser();
    }

    public void register(String email, String password)
    {
        // can also have a thread to answer this call
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new RegisterListener(this, sharedPreferences, email, password));
    }

    public void login(String email, String password, boolean remember)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new LoginListener(this, sharedPreferences, email, password, remember));
    }

    public void credentialsCheck(final Student student)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("students").child(student.numarMatricol);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("Recv", dataSnapshot.toString());
                Student s = dataSnapshot.getValue(Student.class);

                if (s != null) {
                    s.numarMatricol = dataSnapshot.getKey();

                    if (student.equals(s)) {
                        activity.findViewById(R.id.layoutRegister1).setVisibility(View.INVISIBLE);
                        activity.findViewById(R.id.layoutRegister2).setVisibility(View.VISIBLE);
                    } else
                        Toast.makeText(getActivity().getBaseContext(), "Invalid credentials!", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity().getBaseContext(), "Invalid credentials!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void updateUI() {
        if (currentUser != null)
            ;
    }
}
