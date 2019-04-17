package com.lonn.studentassistant.activities.implementations.debug;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.common.DatabasePopulator;

public class DebugActivity extends AppCompatActivity
{
    private DatabasePopulator populator = new DatabasePopulator();

    @Override
    protected void onCreate(Bundle savedBundle)
    {
        super.onCreate(savedBundle);
        setContentView(R.layout.debug_layout);
    }

    public void debugButton(View v)
    {
        switch(v.getId())
        {
            case R.id.deleteStudents:
            {
                populator.deleteStudentsTable();
                break;
            }
            case R.id.populateStudents:
            {
                populator.populateStudentsTable();
                break;
            }
            case R.id.deleteUsers:
            {
                populator.deleteUsersTable();
                break;
            }
            case R.id.populateCourses:
                populator.populateCoursesTable();
                break;
        }
    }
}
