package com.lonn.studentassistant.activities.implementations.debug;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.debug.DatabasePopulator;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;

public class DebugActivity extends FirebaseConnectedActivity {
    private DatabasePopulator populator;

    public void debugButton(View v) {
        switch (v.getId()) {
            case R.id.parse: {
                populator.parseSchedule();
                break;
            }
            case R.id.deleteUsers: {
                populator.deleteUsersTable();
                break;
            }
            case R.id.deleteStudents: {
                populator.deleteStudentsTable();
                break;
            }
            case R.id.populateStudents: {
                populator.populateStudentsTable();
                break;
            }
            case R.id.deleteCourses: {
                populator.deleteCoursesTable();
                break;
            }
            case R.id.deleteProfessors: {
                populator.deleteProfessorsTable();
                break;
            }
            case R.id.deleteGrades: {

            }
            case R.id.populateGrades: {

            }
            case R.id.testDatabase: {
                testDatabase();
            }
            case R.id.populateAdministrators: {
                populator.populateAdministratorsTable();
                break;
            }
        }
    }

    protected void inflateLayout() {
        setContentView(R.layout.debug_layout);
    }

    @Override
    protected void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);

        populator = new DatabasePopulator(FirebaseConnection.getInstance(getBaseContext()), this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 2);
        }
    }

    private void testDatabase() {

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("dev/Students");
        database.orderByChild("studentId").equalTo("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeEventListener(this);

                for (DataSnapshot snapEntity : dataSnapshot.getChildren()) {
                    Log.e("Read", snapEntity.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });


//
//        firebaseConnection.execute(new GetRequest<Student>()
//                .databaseTable(DatabaseTable.STUDENTS)
//                .subscribe(false)
//                .onSuccess(students -> {
//                    for (Student student : students)
//                        Log.e("Student", student.toString());
//                })
//                .onError(error -> Log.e("Error", error.getMessage())));
    }
}
