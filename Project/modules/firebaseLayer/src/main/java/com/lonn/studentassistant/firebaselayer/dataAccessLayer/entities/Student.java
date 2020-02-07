package com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.abstractions.Person;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.CycleSpecializationYear;

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
public final class Student extends Person implements Cloneable {
	private String studentId;
	private String fatherInitial;
	private String group;

	private CycleSpecializationYear cycleSpecializationYear;

	private List<String> otherActivities = new LinkedList<>();
	private List<String> courses = new LinkedList<>();
	private List<String> grades = new LinkedList<>();

	public Student setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public Student setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public Student setEmail(String email) {
		this.email = email;
		return this;
	}

	public Student setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

	public Student setImageMetadataKey(String imageMetadataKey) {
		this.imageMetadataKey = imageMetadataKey;
		return this;
	}
}
