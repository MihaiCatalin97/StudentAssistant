package com.lonn.studentassistant.entities;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.UUID;

@IgnoreExtraProperties
public class Student implements Serializable
{
    @Exclude
    public String numarMatricol;

    public String nume;
    public String prenume;
    public String initialaTatalui;
    public String email;
    public String numarTelefon;
    public int an;
    public String grupa;
    public String accountId;

    public Student(String numarMatricol, String nume, String prenume, String initialaTatalui, String email,
                   String numarTelefon, int an, String grupa)
    {
        this.numarMatricol = numarMatricol;
        this.nume = nume;
        this.prenume = prenume;
        this.initialaTatalui = initialaTatalui;
        this.email = email;
        this.numarTelefon = numarTelefon;
        this.an = an;
        this.grupa = grupa;
    }

    public Student()
    {}

    @Override
    public boolean equals(Object s)
    {
        if (!(s instanceof Student))
            return false;

        Student x = (Student)s;

        if (!x.numarMatricol.equals(this.numarMatricol) ||
            !x.nume.equals(this.nume) ||
            !x.prenume.equals(this.prenume) ||
            !x.numarTelefon.equals(this.numarTelefon) ||
            !x.email.equals(this.email) ||
            !x.initialaTatalui.equals(this.initialaTatalui) ||
            !x.grupa.equals(this.grupa) ||
            x.an != this.an)
            return false;

        return true;
    }
}
