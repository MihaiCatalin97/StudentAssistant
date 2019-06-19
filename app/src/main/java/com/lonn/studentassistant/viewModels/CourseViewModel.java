package com.lonn.studentassistant.viewModels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Professor;

import java.util.ArrayList;
import java.util.List;

public class CourseViewModel extends BaseObservable
{
    @Bindable
    public String courseName, description, website;

    public int pack, semester, year;
    public List<Professor> professors;

    public CourseViewModel(Course course)
    {
        update(course);
    }

    public void update(Course course)
    {
        this.courseName = course.courseName;
        this.pack = course.pack;
        this.semester = course.semester;
        this.year = course.year;
        this.description = course.description;
        this.professors = new ArrayList<>(course.professorEntities);
        this.website = course.website;

        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public String getCourseType()
    {
        if(pack == 0)
            return "Mandatory course";
        else
            return "Optional course (Pack " + Integer.toString(pack) + ")";
    }

    @Bindable
    public String getCourseYearSemester()
    {
        return Utils.yearToString(year) + ", " + Utils.semesterToString(semester);
    }

    @Bindable
    public int getWebsiteVisible()
    {
        return (website != null)? View.VISIBLE : View.GONE;
    }

}
