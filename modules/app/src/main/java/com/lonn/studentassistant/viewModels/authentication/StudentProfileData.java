package com.lonn.studentassistant.viewModels.authentication;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.entities.Student;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecializationYear;

import lombok.ToString;

@ToString
public class StudentProfileData extends BaseObservable {
	/*TODO: Investigate transport method for hashing inside the firebaseLayer
	 * */
	@Bindable
	public String studentId;
	@Bindable
	public String lastName;
	@Bindable
	public String firstName;
	@Bindable
	public String fatherInitial;
	@Bindable
	public String phoneNumber;
	@Bindable
	public String email;
	@Bindable
	public String group;
	@Bindable
	public CycleSpecializationYear cycleSpecializationYear;

	@Bindable
	public String getGroup() {
		if (group == null) {
			return "(none)";
		}

		return group;
	}

	@Bindable
	public String getYear() {
		if (cycleSpecializationYear == null) {
			return "(none)";
		}

		return cycleSpecializationYear.toString();
	}

	public Student toStudent() {
		return new Student()
				.setFatherInitial(fatherInitial)
				.setFirstName(firstName)
				.setLastName(lastName)
				.setPhoneNumber(phoneNumber)
				.setStudentId(studentId)
				.setEmail(email)
				.setYear(cycleSpecializationYear.getYear())
				.setGroup(group);
	}
}
