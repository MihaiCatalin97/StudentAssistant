package com.lonn.studentassistant.viewModels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.view.View;

import com.lonn.studentassistant.BR;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.entities.OtherActivity;
import com.lonn.studentassistant.entities.Professor;

import java.util.ArrayList;
import java.util.List;

public class OtherActivityViewModel extends BaseObservable
{
    @Bindable
    public String activityName, description, website, type;

    public int semester, year;
    public List<Professor> professors;

    public OtherActivityViewModel(OtherActivity activity)
    {
        update(activity);
    }

    public void update(OtherActivity activity)
    {

        this.activityName = activity.activityName;
        this.type = activity.type;
        this.semester = activity.semester;
        this.year = activity.year;
        this.description = activity.description;
        this.professors = new ArrayList<>(activity.professorEntities);
        this.website = activity.website;

        notifyPropertyChanged(BR._all);
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
