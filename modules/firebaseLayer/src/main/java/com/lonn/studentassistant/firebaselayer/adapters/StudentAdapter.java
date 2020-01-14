package com.lonn.studentassistant.firebaselayer.adapters;

import com.lonn.studentassistant.firebaselayer.adapters.abstractions.ViewModelAdapter;
import com.lonn.studentassistant.firebaselayer.entities.Student;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;

import static com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel.builder;

public class StudentAdapter extends ViewModelAdapter<Student, StudentViewModel> {
	public StudentViewModel adapt(Student student) {
		return builder().firstName(student.getFirstName())
				.lastName(student.getLastName())
				.email(student.getEmail())
				.phoneNumber(student.getPhoneNumber())
				.group(student.getGroup())
				.year(student.getYear())
				.studentId(student.getStudentId())
				.cycleSpecialization(student.getCycleAndSpecialization())
				.optionalCourses(student.getOptionalCourses())
				.otherActivities(student.getOtherActivities())
				.imageMetadataKey(student.getImageMetadataKey())
				.build()
				.setKey(student.getKey());
	}

	public Student adapt(StudentViewModel studentViewModel) {
		Student result = new Student()
				.setFirstName(studentViewModel.getFirstName())
				.setLastName(studentViewModel.getLastName())
				.setEmail(studentViewModel.getEmail())
				.setPhoneNumber(studentViewModel.getPhoneNumber())
				.setGroup(studentViewModel.getGroup())
				.setYear(studentViewModel.getYear())
				.setStudentId(studentViewModel.getStudentId())
				.setOptionalCourses(studentViewModel.getOptionalCourses())
				.setOtherActivities(studentViewModel.getOtherActivities())
				.setCycleAndSpecialization(studentViewModel.getCycleSpecialization())
				.setImageMetadataKey(studentViewModel.getImageMetadataKey());

		if (studentViewModel.getKey() != null) {
			result.setKey(studentViewModel.getKey());
		}

		return result;
	}
}
