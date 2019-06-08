package com.lonn.studentassistant.viewModels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Professor;

import java.util.List;

public class ProfessorViewModel extends BaseObservable
{
    @Bindable
    public String firstName, lastName, rank, email, phoneNumber;
    public List<Course> courses;

    public ProfessorViewModel(Professor professor)
    {
        this.firstName = professor.firstName;
        this.lastName = professor.lastName;
        this.rank = professor.level;
        this.email = professor.email;
        this.phoneNumber = professor.phoneNumber;
    }

    public void update(Professor newProfessor)
    {
        if (!firstName.equals(newProfessor.firstName))
        {
            this.firstName = newProfessor.firstName;
            this.notifyPropertyChanged(BR.firstName);
        }
        if (!lastName.equals(newProfessor.lastName))
        {
            this.lastName = newProfessor.lastName;
            this.notifyPropertyChanged(BR.lastName);
        }
        if (!rank.equals(newProfessor.level))
        {
            this.rank = newProfessor.level;
            this.notifyPropertyChanged(BR.rank);
        }
        if (!email.equals(newProfessor.email))
        {
            this.email = newProfessor.email;
            this.notifyPropertyChanged(BR.email);
        }
        if (!phoneNumber.equals(newProfessor.phoneNumber))
        {
            this.phoneNumber = newProfessor.phoneNumber;
            this.notifyPropertyChanged(BR.phoneNumber);
        }
    }

    @Bindable
    public String getFullName()
    {
        StringBuilder result = new StringBuilder();

        if(rank != null && rank.length() > 1)
            result.append(rank).append(" ");
        if (lastName != null && lastName.length() > 1)
            result.append(lastName).append(" ");
        if (firstName != null && firstName.length() > 1)
            result.append(firstName).append(" ");

        return result.toString();
    }

    public void updateCourse(Course course)
    {
        if(!courses.contains(course))
        {
            boolean exists = false;
            int index = 0;

            for (int i = 0; i < courses.size(); i++)
            {
                if (courses.get(i).getKey().equals(course.getKey()))
                {
                    exists = true;
                    index = i;
                    break;
                }
            }

            if (exists)
            {
                courses.remove(index);
                courses.add(course);
            }
        }
        else
        {
            courses.add(course);
        }
    }
}
