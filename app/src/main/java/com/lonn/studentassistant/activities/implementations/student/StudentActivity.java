package com.lonn.studentassistant.activities.implementations.student;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.NavBarActivity;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.databinding.StudentActivityMainLayoutBinding;
import com.lonn.studentassistant.firebaselayer.models.Course;
import com.lonn.studentassistant.firebaselayer.models.OtherActivity;
import com.lonn.studentassistant.firebaselayer.models.Professor;
import com.lonn.studentassistant.firebaselayer.models.ScheduleClass;
import com.lonn.studentassistant.firebaselayer.models.Student;
import com.lonn.studentassistant.viewModels.StudentViewModel;
import com.lonn.studentassistant.views.abstractions.ScrollViewCategory;

public class StudentActivity extends NavBarActivity<Student> {
    public ScrollViewCategory<Course> coursesBaseCategory;
    public ScrollViewCategory<Professor> professorsBaseCategory;
    public ScrollViewCategory<OtherActivity> otherActivitiesBaseCategory;
    public ScrollViewCategory<ScheduleClass> scheduleClassBaseCategory;
    public ScrollViewCategory<Course> coursesProfileCategory;
    public ScrollViewCategory<OtherActivity> otherActivitiesProfileCategory;
    private StudentViewModel studentViewModel;
    private StudentActivityMainLayoutBinding binding;

    public StudentActivity() {
        super();
    }

    public void updateEntity(Student student) {
        if (studentViewModel == null) {
            studentViewModel = new StudentViewModel(student);
            binding.setStudent(studentViewModel);
        }
        else {
            studentViewModel.update(student);
        }
    }

    protected void inflateLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.student_activity_main_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        coursesBaseCategory = findViewById(R.id.coursesBaseCategory);
        professorsBaseCategory = findViewById(R.id.professorsBaseCategory);
        otherActivitiesBaseCategory = findViewById(R.id.otherActivitiesBaseCategory);
        scheduleClassBaseCategory = findViewById(R.id.scheduleBaseCategory);

        ScrollView profilePage = findViewById(R.id.layoutProfile);
        coursesProfileCategory = profilePage.findViewById(R.id.coursesProfileCategory);
        otherActivitiesProfileCategory = profilePage.findViewById(R.id.otherActivitiesProfileCategory);
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.layoutHome).getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        }
        else {
            handleNavBarAction(R.id.nav_home);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        showSnackbar("Loading...");
//        businessLayer.refreshAll();
    }

    public void handleNavBarAction(int id) {
        if (id == R.id.nav_home) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutHome).setVisibility(View.VISIBLE);
        }
        else if (id == R.id.nav_profile) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutProfile).setVisibility(View.VISIBLE);
        }
        else if (id == R.id.nav_schedule) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutSchedule).setVisibility(View.VISIBLE);
        }
        else if (id == R.id.nav_grades) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutGrades).setVisibility(View.VISIBLE);
        }
        else if (id == R.id.nav_messages) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutMessages).setVisibility(View.VISIBLE);
        }
        else if (id == R.id.nav_professors) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutProfessors).setVisibility(View.VISIBLE);
        }
        else if (id == R.id.nav_courses) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutCourses).setVisibility(View.VISIBLE);
        }
        else if (id == R.id.nav_administrative) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutAdministrative).setVisibility(View.VISIBLE);
        }
        else if (id == R.id.nav_otherActivities) {
            Utils.hideViews(Utils.getVisibleChildren(findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutOtherActivities).setVisibility(View.VISIBLE);
        }
    }
}
