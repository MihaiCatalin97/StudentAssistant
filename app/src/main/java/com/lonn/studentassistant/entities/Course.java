package com.lonn.studentassistant.entities;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Course extends BaseEntity implements Comparable<Course>
{
    @Exclude
    public String courseName;
    public int year;
    public int semester;
    public int pack;
    public List<String> professors = new ArrayList<>();
    public String description = "";
    public String website;
    @Exclude
    public List<Professor> professorEntities = new ArrayList<>();

    public int compareTo(Course course)
    {
        return (year - course.year) * 2 + semester - course.semester;
    }

    public Course()
    {}

    public Course(String courseName, int year, int semester, int pack, String description, String website, List<Professor> professors)
    {
        this.courseName = courseName;
        this.year = year;
        this.semester = semester;
        this.pack = pack;
        this.description = description;

        if(professors != null)
        {
            for(int i=0;i<professors.size();i++)
                this.professors.add(professors.get(i).getKey());

            professorEntities = new ArrayList<>(professors);
        }
    }

    public void addProfessor(Professor professor)
    {
        professors.add(professor.getKey());
    }

    public void addProfessor(String professorKey)
    {
        professors.add(professorKey);
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
