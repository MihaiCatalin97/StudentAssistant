package com.lonn.studentassistant.firebaselayer.entities;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.Person;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public final class Professor extends Person {
	private String scheduleLink;
	private String level;
	private String website;
	private String cabinet;

	private String professorImageMetadataKey;

	private List<String> filesMetadata = new LinkedList<>();
	private List<String> courses = new LinkedList<>();
	private List<String> otherActivities = new LinkedList<>();
	private List<String> recurringClasses = new LinkedList<>();
	private List<String> oneTimeClasses = new LinkedList<>();

	public void addCourse(String courseKey) {
		if (!courses.contains(courseKey)) {
			courses.add(courseKey);
		}
	}

	public void addOtherActivity(String otherActivityKey) {
		if (!otherActivities.contains(otherActivityKey)) {
			otherActivities.add(otherActivityKey);
		}
	}

	public void addRecurringClass(String scheduleClassKey) {
		if (!recurringClasses.contains(scheduleClassKey)) {
			recurringClasses.add(scheduleClassKey);
		}
	}

	public void addOneTimeClass(String scheduleClassKey) {
		if (!oneTimeClasses.contains(scheduleClassKey)) {
			oneTimeClasses.add(scheduleClassKey);
		}
	}

	public Professor setFileMetadataKeys(List<String> fileMetadataKeys) {
		this.fileMetadataKeys = fileMetadataKeys;
		return this;
	}

	public Professor setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public Professor setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public Professor setEmail(String email) {
		this.email = email;
		return this;
	}

	public Professor setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}
}
