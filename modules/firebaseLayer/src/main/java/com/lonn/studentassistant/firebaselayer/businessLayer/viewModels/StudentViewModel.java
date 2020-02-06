package com.lonn.studentassistant.firebaselayer.businessLayer.viewModels;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Student;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.CycleSpecializationYear;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.PersonViewModel;

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
public final class StudentViewModel extends PersonViewModel<Student> {
	@Bindable
	private String firstName, lastName, email, phoneNumber, website, group, studentId, fatherInitial;
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

	public StudentViewModel setImageMetadataKey(String imageMetadataKey) {
		this.imageMetadataKey = imageMetadataKey;

		return this;
	}
}
