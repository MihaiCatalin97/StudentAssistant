package com.lonn.studentassistant.entities;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.LinkedList;
import java.util.List;

@IgnoreExtraProperties
public class Professor extends BaseEntity implements Comparable<Professor>
{
    @Exclude
    private String professorKey;
    @Exclude
    public String scheduleLink;

    public String firstName, lastName, email, phoneNumber, level, website, cabinet;

    public List<String> courses = new LinkedList<>();

    public Professor()
    {}

    public Professor(String firstName, String lastName, String email, String phoneNumber, String level, String website, String cabinet)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.level = level;
        this.website = website;
        this.cabinet = cabinet;
    }

    public int compareTo(Professor professor)
    {
        int result = (lastName!=null&&lastName.length()>0?lastName.charAt(0):0) - (professor.lastName!=null&&professor.lastName.length()>0?professor.lastName.charAt(0):0);

        if (result == 0)
            result = (firstName!=null&&firstName.length()>0?firstName.charAt(0):0) - (professor.firstName!=null&&professor.firstName.length()>0?professor.firstName.charAt(0):0);

        return result;
    }

    @Exclude
    public String getKey()
    {
        return (level + "_" + firstName + "_" + lastName).replace(".", "~");
    }

    @Exclude
    public void setKey(String key)
    {
        String[] aux = key.replace("~",".").split("_");
        level = aux[0];
        firstName = aux[1];
        lastName = aux[2];
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Professor))
            return false;

        Professor p = (Professor) o;

        return p.lastName.equals(lastName) &&
                p.firstName.equals(firstName) &&
                p.email.equals(email) &&
                p.level.equals(level) &&
                p.phoneNumber.equals(phoneNumber);
    }

}
