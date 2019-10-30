package com.lonn.studentassistant.entities;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@IgnoreExtraProperties
public class OtherActivity extends BaseEntity
{
    @Exclude
    public String activityName;
    public int year;
    public int semester;
    public List<String> professors = new LinkedList<>();
    public List<String> scheduleClasses = new LinkedList<>();
    public String description = "";
    public String website;
    public String type;

    public int compareTo(Object activity)
    {
        if(!(activity instanceof OtherActivity))
            return -1;
        else
            return activityName.compareTo(((OtherActivity)activity).activityName);
    }

    @Exclude
    public List<Professor> professorEntities = new ArrayList<>();

    public OtherActivity()
    {}

    public OtherActivity(String activityName, int year, int semester, String description, String website, List<Professor> professors, String type)
    {
        this.activityName = activityName;
        this.year = year;
        this.semester = semester;
        this.description = description;

        if(professors != null)
        {
            for(int i=0;i<professors.size();i++)
                this.professors.add(professors.get(i).getKey());

            professorEntities = new ArrayList<>(professors);
        }

        this.type = type;
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
        return activityName.replace(".", "~");
    }

    @Exclude
    public void setKey(String key)
    {
        activityName = key.replace("~", ".");
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof OtherActivity))
            return false;

        OtherActivity c = (OtherActivity) o;

        return c.semester == semester && c.year == year && c.activityName.equals(activityName);
    }

    @Override
    @NonNull
    public String toString()
    {
        return activityName;
    }
}
