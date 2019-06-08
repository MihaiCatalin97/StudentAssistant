package com.lonn.studentassistant.activities.implementations.courseEntity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.entities.Course;

public class CourseActivity extends AppCompatActivity
{
    private boolean editPrivilege;
    private Course course;

    @Override
    protected void onCreate(Bundle b)
    {
        super.onCreate(b);
        setContentView(R.layout.course_activity_layout);

        course = (Course)b.getSerializable("course");

        if (course != null)
        {
            ;
        }
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }
}
