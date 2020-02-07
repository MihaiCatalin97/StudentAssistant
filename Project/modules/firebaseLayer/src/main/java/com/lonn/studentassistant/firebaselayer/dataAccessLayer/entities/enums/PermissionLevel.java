package com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.AdministratorViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.StudentViewModel;

public enum PermissionLevel {
	NONE(0),
	READ_PUBLIC(1),
	WRITE_ENROLL(2),
	READ_PARTIAL(3),
	READ_FULL(4),
	WRITE_ADD_AGGREGATED(5),
	WRITE_PARTIAL(6),
	WRITE(7);

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
