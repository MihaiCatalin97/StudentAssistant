package com.lonn.studentassistant.firebaselayer.entities.abstractions;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public abstract class Discipline extends FileAssociatedEntity {
	private int semester;
	private String disciplineName;
	private String description = "";
	private String website;
	private String scheduleLink;

	private List<String> pendingStudents = new LinkedList<>();
	private List<String> students = new LinkedList<>();
	private List<String> professors = new LinkedList<>();
	private List<String> recurringClasses = new LinkedList<>();
	private List<String> oneTimeClasses = new LinkedList<>();

	public void addProfessor(String professorKey) {
		if (!professors.contains(professorKey)) {
			professors.add(professorKey);
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

	public Discipline setFileMetadataKeys(List<String> fileMetadataKeys) {
		this.fileMetadataKeys = fileMetadataKeys;
		return this;
	}
}
