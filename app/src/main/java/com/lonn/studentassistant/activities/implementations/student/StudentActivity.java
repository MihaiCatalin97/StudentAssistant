package com.lonn.studentassistant.activities.implementations.student;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.implementations.student.callbacks.CourseCallback;
import com.lonn.studentassistant.activities.implementations.student.callbacks.OtherActivityCallback;
import com.lonn.studentassistant.activities.implementations.student.callbacks.ProfessorsCallback;
import com.lonn.studentassistant.common.requests.GetAllRequest;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.OtherActivity;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.services.implementations.otherActivityService.OtherActivityService;
import com.lonn.studentassistant.views.implementations.scrollViewLayouts.CoursesFullScrollView;
import com.lonn.studentassistant.views.implementations.scrollViewLayouts.OtherActivityFullScrollView;
import com.lonn.studentassistant.views.implementations.scrollViewLayouts.ProfessorsFullScrollView;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.activities.abstractions.NavBarActivity;
import com.lonn.studentassistant.services.implementations.coursesService.CourseService;
import com.lonn.studentassistant.services.implementations.professorService.ProfessorService;

public class StudentActivity extends NavBarActivity
{
    private CourseCallback courseCallback = new CourseCallback(this);
    private ProfessorsCallback professorsCallback = new ProfessorsCallback(this);
    private OtherActivityCallback otherActivityCallback = new OtherActivityCallback(this);

    public CoursesFullScrollView coursesFullScrollView;
    public ProfessorsFullScrollView professorsScrollViewLayout;
    public OtherActivityFullScrollView otherActivityFullScrollView;

    public StudentActivity()
    {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.student_activity_main_layout);
        super.onCreate(savedInstanceState);

        coursesFullScrollView = findViewById(R.id.layoutCoursesCategories);
        professorsScrollViewLayout = findViewById(R.id.layoutProfessorsCategories);
        otherActivityFullScrollView = findViewById(R.id.layoutOtherActivitiesCategories);
    }

    @Override
    public void onBackPressed()
    {
        if(findViewById(R.id.layoutHome).getVisibility() == View.VISIBLE)
            super.onBackPressed();
        else
            handleNavBarAction(R.id.nav_home);
    }

    @Override
    public void onStart() {
        super.onStart();

        showSnackbar("Loading...");
        refreshAll();
    }

    public void handleNavBarAction(int id)
    {
        if (id == R.id.nav_home)
        {
            Utils.hideViews(Utils.getVisibleChildren((ViewGroup)findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutHome).setVisibility(View.VISIBLE);
        }
        else if (id == R.id.nav_schedule)
        {
            Utils.hideViews(Utils.getVisibleChildren((ViewGroup)findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutSchedule).setVisibility(View.VISIBLE);
        }
        else if (id == R.id.nav_grades)
        {
            Utils.hideViews(Utils.getVisibleChildren((ViewGroup)findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutGrades).setVisibility(View.VISIBLE);
        }
        else if (id == R.id.nav_messages)
        {
            Utils.hideViews(Utils.getVisibleChildren((ViewGroup)findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutMessages).setVisibility(View.VISIBLE);
        }
        else if (id == R.id.nav_professors)
        {
            Utils.hideViews(Utils.getVisibleChildren((ViewGroup)findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutProfessors).setVisibility(View.VISIBLE);
        }
        else if (id == R.id.nav_courses)
        {
            Utils.hideViews(Utils.getVisibleChildren((ViewGroup)findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutCourses).setVisibility(View.VISIBLE);
        }
        else if (id == R.id.nav_administrative)
        {
            Utils.hideViews(Utils.getVisibleChildren((ViewGroup)findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutAdministrative).setVisibility(View.VISIBLE);
        }
        else if (id == R.id.nav_otherActivities)
        {
            Utils.hideViews(Utils.getVisibleChildren((ViewGroup)findViewById(R.id.layoutMain)));
            findViewById(R.id.layoutOtherActivities).setVisibility(View.VISIBLE);
        }
    }

    protected void unbindServices()
    {
        serviceConnections.unbind(courseCallback);
        serviceConnections.unbind(professorsCallback);
        serviceConnections.unbind(otherActivityCallback);
    }

    protected void refreshAll()
    {
        serviceConnections.postRequest(CourseService.class, new GetAllRequest<Course>(), courseCallback);
        serviceConnections.postRequest(ProfessorService.class, new GetAllRequest<Professor>(), professorsCallback);
        serviceConnections.postRequest(OtherActivityService.class, new GetAllRequest<OtherActivity>(), otherActivityCallback);
    }
}
