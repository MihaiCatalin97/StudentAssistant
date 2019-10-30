package com.lonn.studentassistant.viewModels;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.firebaselayer.models.Course;
import com.lonn.studentassistant.firebaselayer.models.Professor;

import java.util.List;

public class ProfessorViewModel extends BaseObservable {
    @Bindable
    public String firstName, lastName, rank, email, phoneNumber, website, cabinet;
    @Bindable
    public int professorImage = R.drawable.ic_default_picture_person;
    public List<Course> courses;

    public ProfessorViewModel(Professor professor) {
        update(professor);
    }

    public void update(Professor professor) {
        this.firstName = professor.getFirstName();
        this.lastName = professor.getLastName();
        this.rank = professor.getLevel();
        this.email = professor.getEmail();
        this.phoneNumber = professor.getPhoneNumber();
        this.website = professor.getWebsite();
        this.cabinet = professor.getCabinet();

        notifyPropertyChanged(com.lonn.studentassistant.BR._all);
    }

    @Bindable
    public String getFullName() {
        StringBuilder result = new StringBuilder();

        if (rank != null && rank.length() > 1) {
            result.append(rank).append(" ");
        }
        if (lastName != null && lastName.length() > 1) {
            result.append(lastName).append(" ");
        }
        if (firstName != null && firstName.length() > 1) {
            result.append(firstName).append(" ");
        }

        return result.toString();
    }

    @Bindable
    public int getEmailVisible() {
        return (email != null && email.contains("@")) ? View.VISIBLE : View.GONE;
    }

    @Bindable
    public int getWebsiteVisible() {
        return (website != null) ? View.VISIBLE : View.GONE;
    }

    @Bindable
    public int getPhoneNumberVisible() {
        return (phoneNumber != null && phoneNumber.length() >= 10) ? View.VISIBLE : View.GONE;
    }

    @Bindable
    public int getCabinetVisible() {
        return (cabinet != null && cabinet.length() >= 2) ? View.VISIBLE : View.GONE;
    }

    @Bindable
    public int getCoursesVisible() {
        return (courses != null && courses.size() > 0) ? View.VISIBLE : View.GONE;
    }
}
