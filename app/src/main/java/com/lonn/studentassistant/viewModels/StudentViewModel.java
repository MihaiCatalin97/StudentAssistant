package com.lonn.studentassistant.viewModels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Student;

import java.util.List;

public class StudentViewModel extends BaseObservable
{
    @Bindable
    public String firstName, lastName, rank, email, phoneNumber, website, group;
    @Bindable
    public int studentImage = R.drawable.ic_default_picture_person, year;
    public List<Course> courses;

    public StudentViewModel(Student student)
    {
        update(student);
    }

    public void update(Student student)
    {
        this.firstName = student.firstName;
        this.lastName = student.lastName;
        this.email = student.email;
        this.phoneNumber = student.phoneNumber;
        this.website = student.fatherInitial;
        this.group = student.group;
        this.year = student.year;

        notifyPropertyChanged(BR._all);
    }
}
