package com.lonn.studentassistant.viewModels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Professor;

import java.util.ArrayList;
import java.util.List;

public class CourseViewModel extends BaseObservable
{
    @Bindable
    public String courseName, description;

    public int pack, semester, year;
    public List<Professor> professors;

    public CourseViewModel(Course course)
    {
        this.courseName = course.courseName;
        this.pack = course.pack;
        this.semester = course.semester;
        this.year = course.year;
        this.description = course.description;
        this.professors = new ArrayList<>(course.professorEntities);
    }

    public void update(Course newCourse)
    {
        if (!courseName.equals(newCourse.courseName))
        {
            this.courseName = newCourse.courseName;
            this.notifyPropertyChanged(BR.courseName);
        }
        if (pack != newCourse.pack)
        {
            this.pack = newCourse.pack;
            this.notifyPropertyChanged(BR.courseType);
        }
        if (semester != newCourse.semester)
        {
            this.semester = newCourse.semester;
            this.notifyPropertyChanged(BR.courseYearSemester);
        }
        if (year != newCourse.year)
        {
            this.year = newCourse.year;
            this.notifyPropertyChanged(BR.courseYearSemester);
        }
        if (!description.equals(newCourse.description))
        {
            this.description = newCourse.description;
            this.notifyPropertyChanged(BR.description);
        }
        if (!professors.equals(newCourse.professorEntities))
        {
            this.description = newCourse.description;
            this.notifyPropertyChanged(BR.description);
        }
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
}
