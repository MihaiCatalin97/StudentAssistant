package com.lonn.studentassistant.viewModels.entities.abstractions;

import android.view.View;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.Utils;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.Discipline;
import com.lonn.studentassistant.viewModels.entities.OneTimeClassViewModel;
import com.lonn.studentassistant.viewModels.entities.ProfessorViewModel;
import com.lonn.studentassistant.viewModels.entities.RecurringClassViewModel;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
public abstract class DisciplineViewModel<T extends Discipline> extends EntityViewModel<T> {
    @Bindable
    public String name, description, website;

    public int semester, year;
    public List<ProfessorViewModel> professors = new ArrayList<>();
    public List<RecurringClassViewModel> recurringClasses = new ArrayList<>();
    public List<OneTimeClassViewModel> oneTimeClasses = new ArrayList<>();

    @Bindable
    public String getYearSemester() {
        return Utils.yearToString(year) + ", " + Utils.semesterToString(semester);
    }

    @Bindable
    public int getWebsiteVisible() {
        return (website != null) ? View.VISIBLE : View.GONE;
    }
}
