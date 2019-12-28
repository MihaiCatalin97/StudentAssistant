package com.lonn.studentassistant.viewModels.entities;

import android.view.View;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.Professor;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProfessorViewModel extends EntityViewModel<Professor> {
    @Bindable
    public String firstName, lastName, rank, email, phoneNumber, website, cabinet;
    @Bindable
    public String professorImage;
    public List<CourseViewModel> courses;

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
