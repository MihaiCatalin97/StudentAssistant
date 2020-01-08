package com.lonn.studentassistant.firebaselayer.viewModels;

import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.entities.Student;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecialization;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StudentViewModel extends EntityViewModel<Student> {
	@Bindable
	public String firstName, lastName, email, phoneNumber, website, group;
	@Bindable
	public int studentImage, year;
	public List<String> optionalCourses;
	public List<String> otherActivities;
	public CycleSpecialization cycleSpecialization;
}
