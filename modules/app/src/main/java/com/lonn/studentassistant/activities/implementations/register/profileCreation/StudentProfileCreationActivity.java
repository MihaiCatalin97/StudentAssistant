package com.lonn.studentassistant.activities.implementations.register.profileCreation;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.implementations.register.accountCreation.StudentAccountCreationActivity;
import com.lonn.studentassistant.databinding.SingleChoiceListAdapter;
import com.lonn.studentassistant.databinding.StudentProfileCreationActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.api.FirebaseApi;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecializationYear;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.validation.ValidationResult;
import com.lonn.studentassistant.validation.validators.StudentValidator;

import static com.lonn.studentassistant.BR._all;
import static com.lonn.studentassistant.firebaselayer.entities.enums.AccountType.STUDENT;
import static java.util.UUID.randomUUID;

public class StudentProfileCreationActivity extends ProfileCreationActivity<StudentViewModel> {
	private static final Logger LOGGER = Logger.ofClass(StudentProfileCreationActivity.class);
	private StudentProfileCreationActivityLayoutBinding binding;
	private StudentValidator studentValidator = new StudentValidator();

	public StudentProfileCreationActivity() {
		super(STUDENT);
		setPersonProfile(new StudentViewModel());
		getPersonProfile().setKey(randomUUID().toString());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		firebaseApi = FirebaseApi.getApi(this);
		inflateLayout();
		binding.setStudentProfile(getPersonProfile());
	}

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.student_profile_creation_activity_layout);
	}

	public void tapContinue(View view) {
		ValidationResult validationResult = studentValidator.validate(getPersonProfile());

		if (!validationResult.isValid()) {
			Toast.makeText(this.getBaseContext(),
					validationResult.getErrorMessage(),
					Toast.LENGTH_LONG)
					.show();

			return;
		}

		firebaseApi.getStudentService()
				.getByStudentId(getPersonProfile().getStudentId(), false)
				.onSuccess(student -> {
					if (student != null) {
						checkIfPersonHasAccountAndStartActivity(student);
					}
					else {
						startAccountCreationActivity();
					}
				})
				.onError(error -> logAndShowErrorSnack("An error occurred, please try again later",
						error,
						LOGGER));
	}

	protected StudentViewModel mergeExistingProfile(StudentViewModel existingProfile,
													StudentViewModel newProfile) {
		existingProfile.setFirstName(newProfile.getFirstName())
				.setLastName(newProfile.getLastName())
				.setCycleSpecializationYear(newProfile.getCycleSpecializationYear());

		if (newProfile.getEmail() != null && newProfile.getEmail().length() > 0) {
			existingProfile.setEmail(newProfile.getEmail());
		}
		if (newProfile.getFatherInitial() != null && newProfile.getFatherInitial().length() > 0) {
			existingProfile.setFatherInitial(newProfile.getFatherInitial());
		}
		if (newProfile.getPhoneNumber() != null && newProfile.getPhoneNumber().length() > 0) {
			existingProfile.setPhoneNumber(newProfile.getPhoneNumber());
		}

		return existingProfile;
	}

	public void selectGroup(View view) {
		if (getPersonProfile().getCycleSpecializationYear() == null) {
			SingleChoiceListAdapter<CycleSpecializationYear> yearAdapter = new SingleChoiceListAdapter<>(this,
					CycleSpecializationYear.values());

			new AlertDialog.Builder(this, R.style.DialogTheme)
					.setTitle("Select year")
					.setAdapter(yearAdapter, (dialog1, which1) -> {
						CycleSpecializationYear selectedCycleSpecialization = yearAdapter.getItem(which1);

						if (selectedCycleSpecialization != null) {
							showGroupSelectionDialog(selectedCycleSpecialization);
						}
					})
					.create()
					.show();
		}
		else {
			showGroupSelectionDialog(getPersonProfile().getCycleSpecializationYear());
		}
	}

	private void showGroupSelectionDialog(CycleSpecializationYear cycleSpecializationYear) {
		SingleChoiceListAdapter<String> groupAdapter = new SingleChoiceListAdapter<>(this,
				cycleSpecializationYear.getStudentEnrollableGroups());

		new AlertDialog.Builder(this, R.style.DialogTheme)
				.setTitle("Select group")
				.setAdapter(groupAdapter, (dialog2, which2) -> setGroupAndYear(cycleSpecializationYear, groupAdapter.getItem(which2)))
				.create()
				.show();
	}

	public void selectYear(View view) {
		SingleChoiceListAdapter<CycleSpecializationYear> yearAdapter = new SingleChoiceListAdapter<>(this,
				CycleSpecializationYear.values());

		new AlertDialog.Builder(this, R.style.DialogTheme)
				.setTitle("Select year")
				.setAdapter(yearAdapter, (dialog1, which1) -> setYear(yearAdapter.getItem(which1)))
				.create()
				.show();
	}

	private void setGroupAndYear(CycleSpecializationYear cycleSpecializationYear, String group) {
		getPersonProfile().setCycleSpecializationYear(cycleSpecializationYear);
		getPersonProfile().setGroup(group);

		getPersonProfile().notifyPropertyChanged(_all);
	}

	private void setYear(CycleSpecializationYear cycleSpecializationYear) {
		getPersonProfile().setCycleSpecializationYear(cycleSpecializationYear);

		getPersonProfile().notifyPropertyChanged(_all);
	}

	protected Class<StudentAccountCreationActivity> getNextActivityClass() {
		return StudentAccountCreationActivity.class;
	}
}
