package com.lonn.studentassistant.activities.implementations.student.managers;

import com.lonn.studentassistant.activities.implementations.student.customViews.CoursesScrollViewLayout;
import com.lonn.studentassistant.entities.Course;

import java.util.LinkedList;
import java.util.List;

public class StudentActivityCourseManager
{
    private CoursesScrollViewLayout categoriesLayout;
    private List<Course> courses = new LinkedList<>();

    public StudentActivityCourseManager(CoursesScrollViewLayout categoriesLayout)
    {
        this.categoriesLayout = categoriesLayout;
    }

    public void notifyCoursesChanged(List<Course> newCourses)
    {
        courses = newCourses;
        categoriesLayout.update(newCourses);
    }

    private List<Course> getCoursesByYear(int year)
    {
        List<Course> coursesByYear = new LinkedList<>();

        for (Course course : courses)
        {
            if (course.year == year)
                coursesByYear.add(course);
        }

        return coursesByYear;
    }
}
