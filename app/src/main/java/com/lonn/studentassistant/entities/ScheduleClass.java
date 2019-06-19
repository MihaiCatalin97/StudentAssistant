package com.lonn.studentassistant.entities;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@IgnoreExtraProperties
public class ScheduleClass extends BaseEntity
{
    public List<String> rooms;
    public int day;
    public int startHour;
    public int endHour;
    public String courseKey;
    public String type;
    public List<String> professorKeys;
    public String parity;
    public int pack;
    public List<String> groups;

    public int compareTo(Object scheduleClass)
    {
        if(!(scheduleClass instanceof ScheduleClass))
            return -1;

        return (day-((ScheduleClass)scheduleClass).day) * 100000 + startHour-((ScheduleClass)scheduleClass).startHour;
    }

    public ScheduleClass()
    {}

    public ScheduleClass(List<String> room, int day, int startHour, int endHour, String courseKey, String type, List<String> professorKeys, String parity, int pack, List<String> groups)
    {
        this.rooms = new LinkedList<>(room);
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

    @Exclude
    public String getKey()
    {
        StringBuilder result = new StringBuilder();

        for(int i=0;i<rooms.size();i++)
        {
            result.append(rooms.get(i));

            if(i+1<rooms.size())
                result.append("-");
        }

        return result.toString() + "_" + Integer.toString(day) + "_" + Integer.toString(startHour);
    }

    @Exclude
    public void setKey(String key)
    {
        if(key.split("_").length == 3)
        {
            rooms = Arrays.asList(key.split("_")[0].split("-"));
            day = Integer.parseInt(key.split("_")[1]);
            startHour = Integer.parseInt(key.split("_")[2]);
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof ScheduleClass))
            return false;

        ScheduleClass p = (ScheduleClass) o;

        return p.courseKey.equals(courseKey) &&
                p.type.equals(type) &&
                p.parity.equals(parity) &&
                p.rooms.equals(rooms) &&
                p.professorKeys.equals(professorKeys) &&
                p.groups.equals(groups) &&
                p.day == day &&
                p.startHour == startHour &&
                p.endHour == endHour &&
                p.pack == pack;
    }
}
