package com.lonn.studentassistant.entities;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Exam extends BaseEntity
{
    @Exclude
    private String examKey;

    public List<String> rooms;
    public int day;
    public String date;
    public int startHour;
    public int endHour;
    public String courseKey;
    public String type;
    public List<String> professorKeys;
    public int pack;
    public List<String> groups;

    public Exam()
    {}

    public Exam(List<String> rooms, int day, String date, int startHour, int endHour, String courseKey, String type, List<String> professorKeys, int pack, List<String> groups)
    {
        this.rooms = new ArrayList<>(rooms);
        this.day = day;
        this.date = date;
        this.startHour = startHour;
        this.endHour = endHour;
        this.courseKey = courseKey;
        this.type = type;
        this.pack = pack;
        this.groups = new ArrayList<>(groups);

        if(professorKeys != null)
            this.professorKeys = new ArrayList<>(professorKeys);
        else
            this.professorKeys = new ArrayList<>();
    }

    public void addProfessor(Professor professor)
    {
        this.professorKeys.add(professor.getKey());
    }

    @Override
    public String getKey()
    {
        StringBuilder key = new StringBuilder();

        for(String room : rooms)
            key.append(room).append("~");

        key.append("_").append(Integer.toString(day)).append("_").append(Integer.toString(startHour));

        return key.toString();
    }

    @Override
    public void setKey(String key)
    {
        if(key.split("_").length == 3)
        {
            rooms = Arrays.asList(key.split("_")[0].split("~"));
            day = Integer.parseInt(key.split("_")[1]);
            startHour = Integer.parseInt(key.split("_")[2]);
        }
    }
}
