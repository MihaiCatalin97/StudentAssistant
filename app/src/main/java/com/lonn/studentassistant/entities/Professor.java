package com.lonn.studentassistant.entities;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.lonn.studentassistant.R;

import java.util.LinkedList;
import java.util.List;

@IgnoreExtraProperties
public class Professor extends BaseEntity
{
    @Exclude
    public int professorImage = R.drawable.ic_default_picture_person;
    @Exclude
    public String scheduleLink;

    public String firstName, lastName, email, phoneNumber, level, website, cabinet;

    public List<String> courses = new LinkedList<>();
    public List<String> otherActivities = new LinkedList<>();
    public List<String> scheduleClasses = new LinkedList<>();

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

    public int compareTo(Object professor)
    {
        if(!(professor instanceof Professor))
            return -1;

        int result = (lastName!=null&&lastName.length()>0?lastName.charAt(0):0) - (((Professor)professor).lastName!=null&&((Professor)professor).lastName.length()>0?((Professor)professor).lastName.charAt(0):0);

        if (result == 0)
            result = (firstName!=null&&firstName.length()>0?firstName.charAt(0):0) - (((Professor)professor).firstName!=null&&((Professor)professor).firstName.length()>0?((Professor)professor).firstName.charAt(0):0);

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

    @Override
    public Professor clone()
    {
        Professor result = new Professor();

        result.firstName = firstName;
        result.lastName = lastName;
        result.email = email;
        result.phoneNumber = phoneNumber;
        result.level = level;
        result.website = website;
        result.cabinet = cabinet;
        result.professorImage = professorImage;
        result.scheduleLink = scheduleLink;

        result.courses = new LinkedList<>(courses);
        result.otherActivities = new LinkedList<>(otherActivities);
        result.scheduleClasses = new LinkedList<>(scheduleClasses);

        return result;
    }

}
