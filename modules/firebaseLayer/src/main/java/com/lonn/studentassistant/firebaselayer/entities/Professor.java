package com.lonn.studentassistant.firebaselayer.entities;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.FileAssociatedEntity;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.HashableEntity;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public final class Professor extends FileAssociatedEntity implements HashableEntity {
	private String scheduleLink;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String level;
	private String website;
	private String cabinet;

	private String professorImageMetadataKey;

	private List<String> filesMetadata = new LinkedList<>();
	private List<String> courses = new LinkedList<>();
	private List<String> otherActivities = new LinkedList<>();
	private List<String> recurringClasses = new LinkedList<>();
	private List<String> oneTimeClasses = new LinkedList<>();

	@Override
	public String computeHashingString() {
		return getFirstName() + getLastName() + getPhoneNumber();
	}

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
}
