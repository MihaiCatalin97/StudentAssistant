package com.lonn.studentassistant.globalServices.studentService;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lonn.studentassistant.entities.Student;

public class StudentService extends IntentService
{
    private DatabaseReference studentReference;

    public StudentService()
    {
        super("Student Service");

        studentReference = FirebaseDatabase.getInstance().getReference("students");
    }

    @Override
    public void onHandleIntent(final Intent intent)
    {
        final String action = intent.getStringExtra("action");

        if (action.equals("getById"))
        {
            String studentId = intent.getStringExtra("id");

            DatabaseReference ref = studentReference.child(studentId);

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.e(action, dataSnapshot.toString());
                    Student result = dataSnapshot.getValue(Student.class);

                    try {
                        Class callbackClass = Class.forName(intent.getStringExtra("callbackClass"));

                        Intent resultIntent = new Intent(getBaseContext(), callbackClass);
                        resultIntent.putExtra("getById-result", result);
                        resultIntent.putExtras(intent);
                        startService(resultIntent);
                    }
                    catch (Exception e)
                    {
                        Log.e("Error", e.getMessage());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }
}
