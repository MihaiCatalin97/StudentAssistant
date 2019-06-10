package com.lonn.studentassistant.viewModels;

import android.databinding.Bindable;
import android.view.View;

import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.entities.Course;
import com.lonn.studentassistant.entities.Professor;

import java.util.ArrayList;
import java.util.List;

public class CourseViewModel extends EntityViewModel<Course>
{
    @Bindable
    public String courseName, description, website;

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
        this.website = course.website;
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
        if (description != null && !description.equals(newCourse.description))
        {
            this.description = newCourse.description;
            this.notifyPropertyChanged(BR.description);
        }
        if (professors != null && !professors.equals(newCourse.professorEntities))
        {
            this.description = newCourse.description;
            this.notifyPropertyChanged(BR.description);
        }
        if (website != null && !website.equals(newCourse.website))
        {
            this.website = newCourse.website;
            this.notifyPropertyChanged(BR.website);
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

    @Bindable
    public int getWebsiteVisible()
    {
        return (website != null)? View.VISIBLE : View.GONE;
    }

}
