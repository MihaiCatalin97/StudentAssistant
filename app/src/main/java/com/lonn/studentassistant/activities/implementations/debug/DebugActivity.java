package com.lonn.studentassistant.activities.implementations.debug;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.debug.DatabasePopulator;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;

public class DebugActivity extends AppCompatActivity {
    private DatabasePopulator populator;

    public void debugButton(View v) {
        switch (v.getId()) {
            case R.id.parse: {
                //populator.parseSchedule();
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
        }
    }

    @Override
    protected void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.debug_layout);

        populator = new DatabasePopulator(new FirebaseConnection(getBaseContext()));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 2);
        }
    }
}
