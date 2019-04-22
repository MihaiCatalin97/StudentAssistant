package com.lonn.studentassistant.entities;

import android.support.annotation.Nullable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@IgnoreExtraProperties
public class Student extends BaseEntity
{
    @Exclude
    public String studentId;

    public String lastName;
    public String firstName;
    public String fatherInitial;
    public String email;
    public String phoneNumber;
    public int year;
    public String group;
    public String accountId;
    public List<String> optionalCourses = new ArrayList<>();
    public List<String> grades = new ArrayList<>();

    public Student(String studentId, String lastName, String firstName, String fatherInitial, String email,
                   String phoneNumber, int year, String group, @Nullable List<Course> optionalCourses, @Nullable List<Grade> grades)
    {
        init(studentId, lastName, firstName, fatherInitial, email, phoneNumber, year, group);

        if(optionalCourses != null)
        {
            for (int i=0;i<optionalCourses.size();i++)
                this.optionalCourses.add(optionalCourses.get(i).getKey());
        }

        if(grades != null)
        {
            for (int i=0;i<grades.size();i++)
                this.grades.add(grades.get(i).getKey());
        }
    }

    public Student(String studentId, String lastName, String firstName, String fatherInitial, String email,
                   String phoneNumber, int year, String group)
    {
        init(studentId, lastName, firstName, fatherInitial, email, phoneNumber, year, group);
    }

    private void init(String studentId, String lastName, String firstName, String fatherInitial, String email,
                      String phoneNumber, int year, String group)
    {
        this.studentId = studentId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.fatherInitial = fatherInitial;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.year = year;
        this.group = group;
    }

    public void addCourse(Course course)
    {
        optionalCourses.add(course.getKey());
    }

    public void addCourse(String courseKey)
    {
        optionalCourses.add(courseKey);
    }

    public void addGrade(Grade grade)
    {
        grades.add(grade.getKey());
    }

    public void addGrade(String gradeKey)
    {
        grades.add(gradeKey);
    }

    public Student()
    {}

    @Override
    public boolean equals(Object s)
    {
        if (!(s instanceof Student))
            return false;

        Student x = (Student)s;

        return x.studentId.equals(this.studentId) &&
                x.lastName.equals(this.lastName) &&
                x.firstName.equals(this.firstName) &&
                x.phoneNumber.equals(this.phoneNumber) &&
                x.email.equals(this.email) &&
                x.fatherInitial.equals(this.fatherInitial) &&
                x.group.equals(this.group) &&
                x.year == this.year &&
                x.optionalCourses.containsAll(optionalCourses);
    }

    @Exclude
    public String getKey()
    {
        return studentId;
    }

    @Exclude
    public void setKey(String key)
    {
        studentId = key;
    }
}
