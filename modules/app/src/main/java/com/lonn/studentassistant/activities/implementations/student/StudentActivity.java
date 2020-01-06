package com.lonn.studentassistant.activities.implementations.student;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.Utils;
import com.lonn.studentassistant.activities.abstractions.NavBarActivity;
import com.lonn.studentassistant.databinding.StudentActivityMainLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.Student;
import com.lonn.studentassistant.viewModels.adapters.StudentAdapter;
import com.lonn.studentassistant.viewModels.entities.StudentViewModel;

import static android.view.View.VISIBLE;

public class StudentActivity extends NavBarActivity {
    StudentViewModel studentViewModel;
    StudentActivityMainLayoutBinding binding;

    private StudentActivityFirebaseDispatcher dispatcher;

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.layoutHome).getVisibility() == VISIBLE) {
            super.onBackPressed();
        }
        else {
            handleNavBarAction(R.id.nav_home);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        dispatcher.loadCourses();
        dispatcher.loadProfessors();
        dispatcher.loadOtherActivities();
        dispatcher.loadOneTimeClasses(studentViewModel);
        dispatcher.loadRecurringClasses(studentViewModel);
    }

    public void handleNavBarAction(int id) {
        if (id == R.id.nav_home) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutHome).setVisibility(VISIBLE);
        }
        else if (id == R.id.nav_profile) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutProfile).setVisibility(VISIBLE);
        }
        else if (id == R.id.nav_schedule) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutSchedule).setVisibility(VISIBLE);
        }
        else if (id == R.id.nav_grades) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutGrades).setVisibility(VISIBLE);
        }
        else if (id == R.id.nav_messages) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutMessages).setVisibility(VISIBLE);
        }
        else if (id == R.id.nav_professors) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutProfessors).setVisibility(VISIBLE);
        }
        else if (id == R.id.nav_courses) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutCourses).setVisibility(VISIBLE);
        }
        else if (id == R.id.nav_administrative) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutAdministrative).setVisibility(VISIBLE);
        }
        else if (id == R.id.nav_otherActivities) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutOtherActivities).setVisibility(VISIBLE);
        }
    }

    protected void inflateLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.student_activity_main_layout);
        dispatcher = new StudentActivityFirebaseDispatcher(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {
            Student student = (Student) getIntent().getExtras().get("student");

            if (student != null) {
                studentViewModel = new StudentAdapter(this).adaptOne(student);
            }
        }
    }

}
