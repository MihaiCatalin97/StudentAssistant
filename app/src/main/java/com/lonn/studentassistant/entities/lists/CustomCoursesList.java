package com.lonn.studentassistant.entities.lists;

import com.lonn.studentassistant.entities.Course;

import java.util.List;

public class CustomCoursesList extends CustomList<Course>
{
    public CustomCoursesList() {super();}
    public CustomCoursesList(List<Course> courses)
    {
        super(courses);
    }
}
