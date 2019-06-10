package com.lonn.studentassistant.viewModels;

import android.databinding.Bindable;
import android.view.View;

import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.entities.OtherActivity;
import com.lonn.studentassistant.entities.Professor;

import java.util.ArrayList;
import java.util.List;

public class OtherActivityViewModel extends EntityViewModel<OtherActivity>
{
    @Bindable
    public String activityName, description, website, type;

    public int semester, year;
    public List<Professor> professors;

    public OtherActivityViewModel(OtherActivity activity)
    {
        this.activityName = activity.activityName;
        this.type = activity.type;
        this.semester = activity.semester;
        this.year = activity.year;
        this.description = activity.description;
        this.professors = new ArrayList<>(activity.professorEntities);
        this.website = activity.website;
    }

    public void update(OtherActivity newActivity)
    {
        if (!activityName.equals(newActivity.activityName))
        {
            this.activityName = newActivity.activityName;
            this.notifyPropertyChanged(BR.courseName);
        }
        if (semester != newActivity.semester)
        {
            this.semester = newActivity.semester;
            this.notifyPropertyChanged(BR.courseYearSemester);
        }
        if (year != newActivity.year)
        {
            this.year = newActivity.year;
            this.notifyPropertyChanged(BR.courseYearSemester);
        }
        if (type != null && !type.equals(newActivity.type))
        {
            this.type = newActivity.type;
            this.notifyPropertyChanged(BR.type);
        }
        if (description != null && !description.equals(newActivity.description))
        {
            this.description = newActivity.description;
            this.notifyPropertyChanged(BR.description);
        }
        if (professors != null && !professors.equals(newActivity.professorEntities))
        {
            this.description = newActivity.description;
            this.notifyPropertyChanged(BR.description);
        }
        if (website != null && !website.equals(newActivity.website))
        {
            this.website = newActivity.website;
            this.notifyPropertyChanged(BR.website);
        }
    }

    @Bindable
    public String getActivityType()
    {
        return type;
    }

    @Bindable
    public String getActivityYearSemester()
    {
        return Utils.yearToString(year) + ", " + Utils.semesterToString(semester);
    }

    @Bindable
    public int getWebsiteVisible()
    {
        return (website != null)? View.VISIBLE : View.GONE;
    }
}
