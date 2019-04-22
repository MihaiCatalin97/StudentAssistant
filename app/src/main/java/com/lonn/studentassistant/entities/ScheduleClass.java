package com.lonn.studentassistant.entities;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class ScheduleClass extends BaseEntity
{
    @Exclude
    private String classKey;

    public String room;
    public int day;
    public int startHour;
    public int endHour;
    public String courseKey;
    public String type;
    public List<String> professorKeys;
    public String parity;
    public int pack;
    public List<String> groups;

    public ScheduleClass(String room, int day, int startHour, int endHour, String courseKey, String type, List<String> professorKeys, String parity, int pack, List<String> groups)
    {
        this.room = room;
        this.day = day;
        this.startHour = startHour;
        this.endHour = endHour;
        this.courseKey = courseKey;
        this.type = type;
        this.parity = parity;
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
        return room + "_" + Integer.toString(day) + "_" + Integer.toString(startHour);
    }

    @Override
    public void setKey(String key)
    {
        if(key.split("_").length == 3)
        {
            room = key.split("_")[0];
            day = Integer.parseInt(key.split("_")[1]);
            startHour = Integer.parseInt(key.split("_")[2]);
        }
    }
}
