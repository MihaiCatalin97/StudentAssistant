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
import com.lonn.studentassistant.entities.Student;
import com.lonn.studentassistant.entities.User;
import com.lonn.studentassistant.firebaseDatabase.students.StudentDatabaseController;
import com.lonn.studentassistant.firebaseDatabase.students.StudentRepository;
import com.lonn.studentassistant.firebaseDatabase.users.UserDatabaseController;
import com.lonn.studentassistant.firebaseDatabase.users.UserRepository;

public class RegisterService extends IntentService
{
    private UserRepository userRepository;
    private StudentRepository studentRepository;
    private AuthSharedPrefs authSharedPrefs;

    public RegisterService()
    {
        super("Register Service");
        userRepository = new UserRepository(new UserDatabaseController());
        studentRepository = new StudentRepository(new StudentDatabaseController());

        authSharedPrefs = new AuthSharedPrefs();
    }

    @Override
    public void onHandleIntent(Intent intent) {
        final String email = intent.getStringExtra("email");
        final String password = intent.getStringExtra("password");
        final String privileges = intent.getStringExtra("privileges");
        final Student registeringStudent = (Student) intent.getSerializableExtra("registeringStudent");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Intent intent1 = new Intent("register");

                        if (task.isSuccessful())
                        {
                            userRepository.add(new User(email, registeringStudent.numarMatricol, privileges));
                            studentRepository.add(registeringStudent);

                            authSharedPrefs.rememberCredentials(email, password);
                            intent1.putExtra("result", "success");
                        }
                        else {
                            intent1.putExtra("result", getResources().getString(R.string.invalid_email));
                        }

                        intent1.putExtra("success", task.isSuccessful());
                        intent1.putExtra("email", email);
                        intent1.putExtra("password", password);
                        intent1.putExtra("privileges", privileges);

                        sendBroadcast(intent1);
                    }
                });
    }
}
