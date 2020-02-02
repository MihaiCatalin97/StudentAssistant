package com.lonn.studentassistant.firebaselayer.entities.enums;

import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.viewModels.AdministratorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;

public enum PermissionLevel {
	NONE(0),
	READ_PUBLIC(1),
	READ_PARTIAL(2),
	READ_FULL(3),
	WRITE_ADD_AGGREGATED(4),
	WRITE_PARTIAL(5),
	WRITE(6);

	private int permission;

	PermissionLevel(int permission) {
		this.permission = permission;
	}

	public static PermissionLevel getStudentPermissionForCourse(StudentViewModel student,
																CourseViewModel course) {
		if (student.getCourses().contains(course.getKey()) &&
				course.getStudents().contains(student.getKey())) {
			return READ_FULL;
		}
		return READ_PUBLIC;
	}

	public static PermissionLevel getStudentPermissionForActivity(StudentViewModel student,
																  OtherActivityViewModel activity) {
		if (student.getOtherActivities().contains(activity.getKey()) &&
				activity.getStudents().contains(student.getKey())) {
			return READ_FULL;
		}
		return READ_PUBLIC;
	}

	public static PermissionLevel getProfessorPermissionForCourse(ProfessorViewModel professor,
																  CourseViewModel course) {
		if (professor.getCourses().contains(course.getKey()) &&
				course.getProfessors().contains(professor.getKey())) {
			return WRITE;
		}
		return READ_PUBLIC;
	}

	public static PermissionLevel getProfessorPermissionForActivity(ProfessorViewModel professor,
																	OtherActivityViewModel activity) {
		if (professor.getOtherActivities().contains(activity.getKey()) &&
				activity.getProfessors().contains(professor.getKey())) {
			return WRITE;
		}
		return READ_PUBLIC;
	}

	public static PermissionLevel getAdministratorPermissionForCourse(AdministratorViewModel administrator,
																	  Course course) {
		return WRITE;
	}

	public static PermissionLevel getAdministratorPermissionForActivity(AdministratorViewModel administrator,
																		OtherActivityViewModel activity) {
		return WRITE;
	}

	public boolean isAtLeast(PermissionLevel permissionLevel) {
		return this.permission >= permissionLevel.permission;
	}

	public boolean isAtLeast(int permission) {
		return this.permission >= permission;
	}
}
