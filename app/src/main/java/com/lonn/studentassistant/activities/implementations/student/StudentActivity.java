package com.lonn.studentassistant.activities.implementations.student;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.implementations.student.adapters.ProfessorAdapter;
import com.lonn.studentassistant.activities.implementations.student.callbacks.CourseCallback;
import com.lonn.studentassistant.activities.implementations.student.callbacks.ProfessorsCallback;
import com.lonn.studentassistant.activities.implementations.student.customViews.CoursesScrollViewLayout;
import com.lonn.studentassistant.activities.implementations.student.managers.StudentActivityCourseManager;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.activities.abstractions.NavBarActivity;
import com.lonn.studentassistant.entities.Professor;
import com.lonn.studentassistant.services.implementations.coursesService.CourseService;
import com.lonn.studentassistant.services.implementations.professorService.ProfessorService;

import java.util.ArrayList;

public class StudentActivity extends NavBarActivity
{
    public StudentActivityCourseManager courseManager;

    public ProfessorAdapter professorAdapter;

    private CourseCallback courseCallback = new CourseCallback(this);
    private ProfessorsCallback professorsCallback = new ProfessorsCallback(this);

    public StudentActivity()
    {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.student_activity_main_layout);
        super.onCreate(savedInstanceState);

        professorAdapter = new ProfessorAdapter(this, new ArrayList<Professor>());
        courseManager = new StudentActivityCourseManager((CoursesScrollViewLayout)findViewById(R.id.layoutCoursesCategories));


        ((ListView)findViewById(R.id.listProfessors)).setAdapter(professorAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        serviceConnections.bind(CourseService.class, courseCallback);
        serviceConnections.bind(ProfessorService.class, professorsCallback);
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
    }

    protected void unbindServices()
    {
        serviceConnections.unbind(courseCallback, this);
    }
}
