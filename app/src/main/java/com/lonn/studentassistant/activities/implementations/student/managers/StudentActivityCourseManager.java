package com.lonn.studentassistant.activities.implementations.student.managers;

import com.lonn.studentassistant.views.implementations.CoursesScrollViewLayout;
import com.lonn.studentassistant.entities.Course;

import java.util.List;

public class StudentActivityCourseManager
{
    private CoursesScrollViewLayout categoriesLayout;

    public StudentActivityCourseManager(CoursesScrollViewLayout categoriesLayout)
    {
        this.categoriesLayout = categoriesLayout;
    }

    public void notifyCoursesChanged(List<Course> newCourses)
    {
        categoriesLayout.update(newCourses);
    }
}
