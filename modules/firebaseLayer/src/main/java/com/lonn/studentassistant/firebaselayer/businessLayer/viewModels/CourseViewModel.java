package com.lonn.studentassistant.firebaselayer.businessLayer.viewModels;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.CycleSpecializationYear;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.DisciplineViewModel;

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
	private List<String> grades;

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

	public boolean isForCycleAndYearAndSemester(CycleSpecializationYear cycleSpecializationYear,
												int semester) {
		for (CycleSpecializationYear cSY : cycleSpecializationYears) {
			if (cSY.equals(cycleSpecializationYear) && this.semester == semester) {
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

	@Override
	public CourseViewModel clone() {
		return (CourseViewModel) super.clone();
	}
}
