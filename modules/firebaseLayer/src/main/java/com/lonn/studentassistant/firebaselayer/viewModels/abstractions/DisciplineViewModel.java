package com.lonn.studentassistant.firebaselayer.viewModels.abstractions;

import android.view.View;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.Discipline;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import static com.lonn.studentassistant.firebaselayer.Utils.semesterToString;
import static com.lonn.studentassistant.firebaselayer.Utils.yearToString;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
public abstract class DisciplineViewModel<T extends Discipline> extends EntityViewModel<T> {
	@Bindable
	public String name, description, website;

	public int semester, year;
	public List<String> professors = new ArrayList<>();
	public List<String> recurringClasses = new ArrayList<>();
	public List<String> oneTimeClasses = new ArrayList<>();
	public List<String> filesMetadata = new ArrayList<>();

	@Bindable
	public String getYearSemester() {
		return yearToString(year) + ", " + semesterToString(semester);
	}

	@Bindable
	public int getWebsiteVisible() {
		return (website != null) ? View.VISIBLE : View.GONE;
	}
}
