package com.lonn.studentassistant.firebaselayer.viewModels;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecialization;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecializationYear;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.DisciplineViewModel;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public final class CourseViewModel extends DisciplineViewModel<Course> {
	private int pack;
	private List<CycleSpecializationYear> cycleSpecializationYears;
	private List<String> laboratories;

	@Bindable
	public String getCourseType() {
		if (pack == 0) {
			return "Mandatory discipline";
		}
		else {
			return "Optional discipline (Pack " + pack + ")";
		}
	}

	@Override
	public CourseViewModel setKey(String key) {
		super.setKey(key);
		return this;
	}

	public boolean isForCycleAndYearAndSemester(CycleSpecialization cycleSpecialization, int year,
												int semester) {
		for (CycleSpecializationYear cycleSpecializationYear : cycleSpecializationYears) {
			if (cycleSpecializationYear.getCycleSpecialization().equals(cycleSpecialization) &&
					cycleSpecializationYear.getYear() == year &&
					this.semester == semester) {
				return true;
			}
		}

		return false;
	}

	@Bindable
	public String getCyclesAndSpecializations() {
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < cycleSpecializationYears.size(); i++) {
			stringBuilder.append(cycleSpecializationYears.get(i).toString());

			if (i + 1 < cycleSpecializationYears.size()) {
				stringBuilder.append("\n");
			}
		}

		return stringBuilder.toString();
	}
}
