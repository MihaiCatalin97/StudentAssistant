package com.lonn.studentassistant.entities;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Professor extends BaseEntity
{
    @Exclude
    private String professorKey;
    @Exclude
    public String scheduleLink;

    public String firstName, lastName, email, phoneNumber, level, webSite;

    public Professor()
    {}

    public Professor(String firstName, String lastName, String email, String phoneNumber, String level, String webSite)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.level = level;
        this.webSite = webSite;
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
