package com.lonn.studentassistant.viewModels;

import android.databinding.Bindable;
import android.view.View;

import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.entities.ScheduleClass;

import java.util.LinkedList;
import java.util.List;

public class ScheduleClassViewModel extends EntityViewModel<ScheduleClass>
{
    public String courseKey;
    public List<String> rooms;
    public int day, startHour, endHour;
    public List<String> professorKeys;

    @Bindable
    public String type, parity;
    @Bindable
    public List<String> groups;

    public ScheduleClassViewModel(ScheduleClass scheduleClass)
    {
        this.type = scheduleClass.type;
        this.day = scheduleClass.day;
        this.courseKey = scheduleClass.courseKey;
        this.rooms = new LinkedList<>(scheduleClass.rooms);
        this.groups = new LinkedList<>(scheduleClass.groups);
        this.professorKeys = new LinkedList<>(scheduleClass.professorKeys);
        this.startHour = scheduleClass.startHour;
        this.endHour = scheduleClass.endHour;
        this.parity = scheduleClass.parity;
    }

    public void update(ScheduleClass scheduleClass)
    {
        if (type == null || !type.equals(scheduleClass.type))
        {
            this.type = scheduleClass.type;
            this.notifyPropertyChanged(BR.type);
        }
        if (courseKey == null || !courseKey.equals(scheduleClass.courseKey))
        {
            this.courseKey = scheduleClass.courseKey;
            this.notifyPropertyChanged(BR.courseName);
        }
        if (day != scheduleClass.day)
        {
            this.day = scheduleClass.day;
            this.notifyPropertyChanged(BR.day);
        }
        if (groups == null || !groups.equals(scheduleClass.groups))
        {
            this.groups = new LinkedList<>(scheduleClass.groups);
            this.notifyPropertyChanged(BR.groups);
        }
        if (startHour != scheduleClass.startHour)
        {
            this.startHour = scheduleClass.startHour;
            this.notifyPropertyChanged(BR.startHour);
        }
        if (endHour != scheduleClass.endHour)
        {
            this.endHour = scheduleClass.endHour;
            this.notifyPropertyChanged(BR.endHour);
        }
        if (parity == null || !parity.equals(scheduleClass.parity))
        {
            this.parity = scheduleClass.parity;
            this.notifyPropertyChanged(BR.parity);
            this.notifyPropertyChanged(BR.parityVisible);
        }
    }

    @Bindable
    public int getParityVisible()
    {
        return (parity != null && parity.length()> 0)? View.VISIBLE : View.GONE;
    }

    @Bindable
    public String getStartHour()
    {
        return Integer.toString(startHour/100) + ":" + Integer.toString(startHour%100);
    }

    @Bindable
    public String getEndHour()
    {
        return Integer.toString(endHour/100) + ":" + Integer.toString(endHour%100);
    }

    @Bindable
    public String getDay()
    {
        return Utils.dayToString(day);
    }

    @Bindable
    public String getCourseName()
    {
        int legth=0;
        String[] words = courseKey.split(" ");
        String result = "";

        for(String word : words)
        {
            result += word + " ";
            legth += word.length() + 1;

            if (legth > 20)
            {
                result += "\n";
                legth = 0;
            }
        }

        return result;
    }

    @Bindable
    public String getHours()
    {
        return getStartHour() + "\n" + getEndHour();
    }

    @Bindable
    public String getRooms()
    {
        String result =  "";

        for(int i=0;i<rooms.size();i++)
        {
            result += rooms.get(i);

            if(i+1 < rooms.size())
                result += "\n";
        }

        return result;
    }
}
