package com.lonn.studentassistant.viewModels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Professor;

import java.util.List;

public class ProfessorViewModel extends BaseObservable
{
    @Bindable
    public String firstName, lastName, rank, email, phoneNumber, website, cabinet;
    public List<Course> courses;

    public ProfessorViewModel(Professor professor)
    {
        this.firstName = professor.firstName;
        this.lastName = professor.lastName;
        this.rank = professor.level;
        this.email = professor.email;
        this.phoneNumber = professor.phoneNumber;
        this.website = professor.website;
        this.cabinet = professor.cabinet;
    }

    public void update(Professor newProfessor)
    {
        if (firstName == null || !firstName.equals(newProfessor.firstName))
        {
            this.firstName = newProfessor.firstName;
            this.notifyPropertyChanged(BR.firstName);
        }
        if (lastName == null || !lastName.equals(newProfessor.lastName))
        {
            this.lastName = newProfessor.lastName;
            this.notifyPropertyChanged(BR.lastName);
        }
        if (rank == null || !rank.equals(newProfessor.level))
        {
            this.rank = newProfessor.level;
            this.notifyPropertyChanged(BR.rank);
        }
        if (email == null || !email.equals(newProfessor.email))
        {
            this.email = newProfessor.email;
            this.notifyPropertyChanged(BR.email);
            this.notifyPropertyChanged(BR.emailVisible);
        }
        if (phoneNumber == null || !phoneNumber.equals(newProfessor.phoneNumber))
        {
            this.phoneNumber = newProfessor.phoneNumber;
            this.notifyPropertyChanged(BR.phoneNumber);
            this.notifyPropertyChanged(BR.phoneNumberVisible);
        }
        if (website == null || !website.equals(newProfessor.website))
        {
            this.website = newProfessor.website;
            this.notifyPropertyChanged(BR.website);
            this.notifyPropertyChanged(BR.websiteVisible);
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

    @Bindable
    public int getEmailVisible()
    {
        return (email != null && email.contains("@"))? View.VISIBLE : View.GONE;
    }

    @Bindable
    public int getWebsiteVisible()
    {
        return (website != null)? View.VISIBLE : View.GONE;
    }

    @Bindable
    public int getPhoneNumberVisible()
    {
        return (phoneNumber != null && phoneNumber.length() >= 10)? View.VISIBLE : View.GONE;
    }

    @Bindable
    public int getProfessorImage()
    {
        return R.drawable.ic_default_picture_person;
    }

    @Bindable
    public int getCabinetVisible()
    {
        return (cabinet != null && cabinet.length() >= 2)? View.VISIBLE : View.GONE;
    }

    @Bindable
    public int getCoursesVisible()
    {
        return (courses != null && courses.size() > 0)? View.VISIBLE : View.GONE;
    }

    @BindingAdapter("srcCompat")
    public static void bindSrcCompat(ImageView imageView, int resourceId)
    {
        imageView.setImageResource(resourceId);
    }
}
