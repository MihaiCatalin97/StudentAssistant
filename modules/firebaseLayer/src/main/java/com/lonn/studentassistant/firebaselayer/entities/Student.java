package com.lonn.studentassistant.firebaselayer.entities;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
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
public final class Student extends BaseEntity implements Cloneable {
	private String studentId;
	private String lastName;
	private String firstName;
	private String fatherInitial;
	private String email;
	private String phoneNumber;
	private String group;
	private String imageMetadataKey;
	private int year;

	private CycleSpecialization cycleAndSpecialization;

	private List<String> otherActivities = new LinkedList<>();
	private List<String> courses = new LinkedList<>();
	private List<String> grades = new LinkedList<>();
}
