package com.lonn.studentassistant.firebaselayer.viewModels;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.entities.Student;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecialization;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecializationYear;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public final class StudentViewModel extends EntityViewModel<Student> {
	@Bindable
	private String firstName, lastName, email, phoneNumber, website, group, studentId, fatherInitial;
	@Bindable
	private String imageMetadataKey;
	private List<String> courses;
	private List<String> otherActivities;
	private CycleSpecializationYear cycleSpecializationYear;
	private List<String> gradeKeys;

	@Override
	public StudentViewModel setKey(String key) {
		super.setKey(key);
		return this;
	}

	@Override
	public StudentViewModel clone() {
		return (StudentViewModel) super.clone();
	}
}
