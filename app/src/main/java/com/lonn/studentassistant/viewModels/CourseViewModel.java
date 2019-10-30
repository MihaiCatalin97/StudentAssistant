package com.lonn.studentassistant.viewModels;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.firebaselayer.models.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseViewModel extends BaseObservable {
    @Bindable
    public String courseName, description, website;

    public int pack, semester, year;
    public List<String> professors;

    public CourseViewModel(Course course) {
        update(course);
    }

    public void update(Course course) {
        this.courseName = course.getCourseName();
        this.pack = course.getPack();
        this.semester = course.getSemester();
        this.year = course.getYear();
        this.description = course.getDescription();
        this.professors = new ArrayList<>(course.getProfessors());
        this.website = course.getWebsite();

        notifyPropertyChanged(com.lonn.studentassistant.BR._all);
    }

    @Bindable
    public String getCourseType() {
        if (pack == 0) {
            return "Mandatory course";
        }
        else {
            return "Optional course (Pack " + pack + ")";
        }
    }

    @Bindable
    public String getCourseYearSemester() {
        return Utils.yearToString(year) + ", " + Utils.semesterToString(semester);
    }

    @Bindable
    public int getWebsiteVisible() {
        return (website != null) ? View.VISIBLE : View.GONE;
    }

}
