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

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CourseViewModel extends DisciplineViewModel<Course> {
	public int pack;
	public List<CycleSpecializationYear> cycleSpecializationYears;

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
					this.semester == semester)
				return true;
		}

		return false;
	}
}
