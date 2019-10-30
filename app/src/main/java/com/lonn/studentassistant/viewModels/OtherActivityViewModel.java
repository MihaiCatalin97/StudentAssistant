package com.lonn.studentassistant.viewModels;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.firebaselayer.models.OtherActivity;

import java.util.ArrayList;
import java.util.List;

public class OtherActivityViewModel extends BaseObservable {
    @Bindable
    public String activityName, description, website, type;

    public int semester, year;
    public List<String> professors;

    public OtherActivityViewModel(OtherActivity activity) {
        update(activity);
    }

    public void update(OtherActivity activity) {

        this.activityName = activity.getActivityName();
        this.type = activity.getType();
        this.semester = activity.getSemester();
        this.year = activity.getYear();
        this.description = activity.getDescription();
        this.professors = new ArrayList<>(activity.getProfessors());
        this.website = activity.getWebsite();

        notifyPropertyChanged(com.lonn.studentassistant.BR._all);
    }

    @Bindable
    public String getActivityType() {
        return type;
    }

    @Bindable
    public String getActivityYearSemester() {
        return Utils.yearToString(year) + ", " + Utils.semesterToString(semester);
    }

    @Bindable
    public int getWebsiteVisible() {
        return (website != null) ? View.VISIBLE : View.GONE;
    }
}
