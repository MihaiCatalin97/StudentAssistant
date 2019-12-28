package com.lonn.studentassistant.viewModels.entities;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.entities.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentViewModel extends EntityViewModel<Student> {
    @Bindable
    public String firstName, lastName, email, phoneNumber, website, group;
    @Bindable
    public int studentImage = R.drawable.ic_default_picture_person, year;
    public List<CourseViewModel> courses = new ArrayList<>();

    public StudentViewModel(Student student) {
        update(student);
    }

    public void update(Student student) {
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.email = student.getEmail();
        this.phoneNumber = student.getPhoneNumber();
        this.website = student.getFatherInitial();
        this.group = student.getGroup();
        this.year = student.getYear();

        notifyPropertyChanged(com.lonn.studentassistant.BR._all);
    }
}
