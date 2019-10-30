package com.lonn.studentassistant.entities;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.Date;

public class Grade extends BaseEntity
{
    @Exclude
    private String gradeKey;
    private String studentId, courseId;
    private Date date;
    private int value;

    public Grade()
    {}

    public int compareTo(Object course)
    {
        return 0;
    }

    public Grade(@NonNull String studentId, @NonNull String courseId, @NonNull Date date, int value)
    {
        this.studentId = studentId;
        this.courseId = courseId;
        this.date = date;
        this.value = value;
    }

    @Exclude
    public String getKey()
    {
        return (studentId + "_" + courseId).replace(".", "~");
    }

    @Exclude
    public void setKey(String key)
    {
        String[] aux = key.replace("~",".").split("_");
        studentId = aux[0];
        courseId = aux[1];
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Grade))
            return false;

        Grade p = (Grade) o;

        return p.studentId.equals(studentId) &&
                p.courseId.equals(courseId) &&
                p.date.equals(date) &&
                p.value == value;
    }
}
