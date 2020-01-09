package com.lonn.studentassistant.firebaselayer.entities;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.HashableEntity;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecialization;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Student extends HashableEntity implements Cloneable {
	private String studentId;
	private String lastName;
	private String firstName;
	private String fatherInitial;
	private String email;
	private String phoneNumber;
	private String group;
	private int year;

	private CycleSpecialization cycleAndSpecialization;

	private List<String> otherActivities = new LinkedList<>();
	private List<String> optionalCourses = new LinkedList<>();
	private List<String> grades = new LinkedList<>();

	@Override
	public String computeHashingString() {
		return getStudentId() + getFirstName()
				+ getLastName() + getFatherInitial() + getPhoneNumber();
	}

	public void addOtherActivity(String otherActivityKey) {
		if (!otherActivities.contains(otherActivityKey)) {
			otherActivities.add(otherActivityKey);
		}
	}

	public void addOptionalCourse(String optionalCourseKey) {
		if (!optionalCourses.contains(optionalCourseKey)) {
			optionalCourses.add(optionalCourseKey);
		}
	}

	public void addGrade(String gradeKey) {
		if (!grades.contains(gradeKey)) {
			grades.add(gradeKey);
		}
	}
}
