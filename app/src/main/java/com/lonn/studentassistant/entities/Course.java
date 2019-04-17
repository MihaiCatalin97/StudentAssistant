package com.lonn.studentassistant.entities;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Course extends BaseEntity
{
    @Exclude
    public String courseName;
    public int year;
    public int semester;

    public Course()
    {}

    public Course(String courseName, int year, int semester)
    {
        this.courseName = courseName;
        this.year = year;
        this.semester = semester;
    }

    @Exclude
    public String getKey()
    {
        return courseName.replace(".", "~");
    }

    @Exclude
    public void setKey(String key)
    {
        courseName = key.replace("~", ".");
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Course))
            return false;

        Course c = (Course) o;

        return c.semester == semester && c.year == year && c.courseName.equals(courseName);
    }
}
