package com.lonn.studentassistant.viewModels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.ScheduleClass;

import java.util.LinkedList;
import java.util.List;

public class ScheduleClassViewModel extends BaseObservable
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
        update(scheduleClass);
    }

    public void update(ScheduleClass scheduleClass)
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

        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public int getParityVisible()
    {
        return (parity != null && parity.length()> 0)? View.VISIBLE : View.GONE;
    }

    @Bindable
    public String getStartHour()
    {
        String minuteString = Integer.toString(startHour%100);

        if(minuteString.length() == 1)
            minuteString = "0" + minuteString;

        return Integer.toString(startHour/100) + ":" + minuteString;
    }

    @Bindable
    public String getEndHour()
    {
        String minuteString = Integer.toString(endHour%100);

        if(minuteString.length() == 1)
            minuteString = "0" + minuteString;

        return Integer.toString(endHour/100) + ":" + minuteString;
    }

    @Bindable
    public String getDay()
    {
        return Utils.dayToString(day);
    }

    @Bindable
    public String getCourseName()
    {
        Course auxCourse = new Course();
        auxCourse.setKey(courseKey);

        String courseName = auxCourse.getKey();
        int legth=0;
        String[] words = courseName.split(" ");
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

    @Bindable
    public String getFormattedType()
    {
        return type.replace(" ","\n");
    }

    @Bindable
    public int getTypeNumberOfLines()
    {
        return type.split(" ").length;
    }

    @Bindable
    public int getNumberOfRooms()
    {
        return rooms.size();
    }
}
