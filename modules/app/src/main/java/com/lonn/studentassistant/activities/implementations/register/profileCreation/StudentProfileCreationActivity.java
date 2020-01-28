package com.lonn.studentassistant.activities.implementations.register.profileCreation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FirebaseConnectedActivity;
import com.lonn.studentassistant.activities.implementations.register.AccountCreationActivity;
import com.lonn.studentassistant.databinding.SingleChoiceListAdapter;
import com.lonn.studentassistant.databinding.StudentProfileCreationActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.api.FirebaseApi;
import com.lonn.studentassistant.firebaselayer.entities.enums.AccountType;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecializationYear;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.viewModels.authentication.StudentProfileData;

import static com.lonn.studentassistant.BR._all;
import static com.lonn.studentassistant.firebaselayer.entities.enums.AccountType.STUDENT;
import static com.lonn.studentassistant.validation.Regex.EMAIL_REGEX;
import static com.lonn.studentassistant.validation.Regex.PHONE_NUMBER_REGEX;
import static java.util.UUID.randomUUID;

public class StudentProfileCreationActivity extends FirebaseConnectedActivity {
	private static final Logger LOGGER = Logger.ofClass(StudentProfileCreationActivity.class);
	private StudentProfileData profileData = new StudentProfileData();
	private StudentProfileCreationActivityLayoutBinding binding;
	private AccountType accountType = STUDENT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		firebaseApi = FirebaseApi.getApi(this);
		inflateLayout();
		binding.setStudentProfile(profileData);
	}

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.student_profile_creation_activity_layout);
	}

	private boolean validateInputs() {
		if (profileData.studentId == null || profileData.studentId.length() == 0) {
			showSnackBar("Please enter your student ID", 1000);
			return false;
		}
		if (profileData.firstName == null || profileData.firstName.length() == 0) {
			showSnackBar("Please enter your first name", 1000);
			return false;
		}
		if (profileData.lastName == null || profileData.lastName.length() == 0) {
			showSnackBar("Please enter your last name", 1000);
			return false;
		}
		if (profileData.cycleSpecializationYear == null) {
			showSnackBar("Please enter your year", 1000);
			return false;
		}
		if (profileData.group == null || profileData.group.length() == 0) {
			showSnackBar("Please enter your group", 1000);
			return false;
		}
		if (profileData.phoneNumber != null && profileData.phoneNumber.length() > 0 &&
				profileData.phoneNumber.matches(PHONE_NUMBER_REGEX)) {
			showSnackBar("Invalid phone number", 1000);
			return false;
		}
		if (profileData.email != null && profileData.email.length() > 0 &&
				profileData.email.matches(EMAIL_REGEX)) {
			showSnackBar("Invalid email", 1000);
			return false;
		}

		return true;
	}

	public void tapContinue(View view) {
		if (!validateInputs()) {
			return;
		}

		firebaseApi.getStudentService()
				.getByStudentId(profileData.studentId, false)
				.onSuccess(student -> {
					if (student != null) {
						firebaseApi.getUserService()
								.personHasAccount(student.getKey())
								.onSuccess(result -> {
									if (result) {
										showSnackBar("This student id already has an account associated to id!", 1000);
									}
									else {
										saveProfileAndStartActivity(updateStudentProfile(student, profileData));
									}
								})
								.onError(error -> logAndShowErrorSnack("An error occurred, please try again later",
										error,
										LOGGER));
					}
					else {
						StudentViewModel studentViewModel = parseStudent();
						saveProfileAndStartActivity(studentViewModel);
					}
				})
				.onError(error -> logAndShowErrorSnack("An error occurred, please try again later",
						error,
						LOGGER));
	}

	private StudentViewModel updateStudentProfile(StudentViewModel student,
												  StudentProfileData profileData) {
		student.setFirstName(profileData.firstName)
				.setLastName(profileData.lastName)
				.setCycleSpecialization(profileData.cycleSpecializationYear.getCycleSpecialization())
				.setYear(profileData.cycleSpecializationYear.getYear());

		if (profileData.email != null && profileData.email.length() > 0) {
			student.setEmail(profileData.email);
		}
		if (profileData.fatherInitial != null && profileData.fatherInitial.length() > 0) {
			student.setFatherInitial(profileData.fatherInitial);
		}
		if (profileData.phoneNumber != null && profileData.phoneNumber.length() > 0) {
			student.setPhoneNumber(profileData.phoneNumber);
		}

		return student;
	}

	private void saveProfileAndStartActivity(StudentViewModel studentViewModel) {
		firebaseApi.getStudentService()
				.save(studentViewModel)
				.onSuccess(none -> {
					startAccountCreationActivity(studentViewModel.getKey(), accountType);
				})
				.onError(error -> logAndShowErrorSnack("An error occurred while creating your profile, please try again later",
						error,
						LOGGER));
	}

	private StudentViewModel parseStudent() {
		return new StudentViewModel()
				.setStudentId(profileData.studentId)
				.setPhoneNumber(profileData.phoneNumber)
				.setFirstName(profileData.firstName)
				.setLastName(profileData.lastName)
				.setEmail(profileData.email)
				.setGroup(profileData.group)
				.setCycleSpecialization(profileData.cycleSpecializationYear.getCycleSpecialization())
				.setYear(profileData.cycleSpecializationYear.getYear())
				.setFatherInitial(profileData.fatherInitial)
				.setKey(randomUUID().toString());
	}

	private void startAccountCreationActivity(String personUUID, AccountType accountType) {
		Intent accountCreationActivityIntent = new Intent(this, AccountCreationActivity.class);

		accountCreationActivityIntent.putExtra("personUUID", personUUID);
		accountCreationActivityIntent.putExtra("accountType", accountType.toString());

		startActivity(accountCreationActivityIntent);
	}

	public void selectGroup(View view) {
		if (profileData.cycleSpecializationYear == null) {
			SingleChoiceListAdapter<CycleSpecializationYear> yearAdapter = new SingleChoiceListAdapter<>(this,
					CycleSpecializationYear.values());

			new AlertDialog.Builder(this, R.style.DialogTheme)
					.setTitle("Select year")
					.setAdapter(yearAdapter, (dialog1, which1) -> {
						showGroupSelectionDialog(yearAdapter.getItem(which1));
					})
					.create()
					.show();
		}
		else {
			showGroupSelectionDialog(profileData.cycleSpecializationYear);
		}
	}

	private void showGroupSelectionDialog(CycleSpecializationYear cycleSpecializationYear) {
		SingleChoiceListAdapter<String> groupAdapter = new SingleChoiceListAdapter<>(this,
				cycleSpecializationYear.getStudentEnrollableGroups());

		new AlertDialog.Builder(this, R.style.DialogTheme)
				.setTitle("Select group")
				.setAdapter(groupAdapter, (dialog2, which2) -> {
					setGroupAndYear(cycleSpecializationYear, groupAdapter.getItem(which2));
				})
				.create()
				.show();
	}

	public void selectYear(View view) {
		SingleChoiceListAdapter<CycleSpecializationYear> yearAdapter = new SingleChoiceListAdapter<>(this,
				CycleSpecializationYear.values());

		new AlertDialog.Builder(this, R.style.DialogTheme)
				.setTitle("Select year")
				.setAdapter(yearAdapter, (dialog1, which1) -> {
					setYear(yearAdapter.getItem(which1));
				})
				.create()
				.show();
	}

	private void setGroupAndYear(CycleSpecializationYear cycleSpecializationYear, String group) {
		profileData.cycleSpecializationYear = cycleSpecializationYear;
		profileData.group = group;

		profileData.notifyPropertyChanged(_all);
	}

	private void setYear(CycleSpecializationYear cycleSpecializationYear) {
		profileData.cycleSpecializationYear = cycleSpecializationYear;

		profileData.notifyPropertyChanged(_all);
	}
}
