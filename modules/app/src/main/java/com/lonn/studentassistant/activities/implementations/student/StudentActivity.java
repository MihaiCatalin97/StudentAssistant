package com.lonn.studentassistant.activities.implementations.student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.activities.abstractions.MainActivity;
import com.lonn.studentassistant.databinding.StudentActivityMainLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.ScheduleClassViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.abstractions.ProfileImageUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations.ProfessorFileUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations.StudentImageUploadDialog;

import java.util.Date;

import lombok.Getter;

import static android.view.View.GONE;
import static com.lonn.studentassistant.utils.schedule.Utils.dateOfNextClass;
import static com.lonn.studentassistant.utils.schedule.Utils.getNextClass;
import static com.lonn.studentassistant.utils.schedule.Utils.getNextExam;

public class StudentActivity extends MainActivity<StudentViewModel> {
	private static final Logger LOGGER = Logger.ofClass(StudentActivity.class);
	@Getter
	StudentActivityMainLayoutBinding binding;
	protected Dispatcher<StudentActivity, StudentViewModel> dispatcher;

	protected void loadAll(String entityKey) {
		dispatcher.loadAll(entityKey);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dispatcher = new StudentActivityFirebaseDispatcher(this);

		findViewById(R.id.fabEdit).setOnClickListener(view -> onEditTapped());
		findViewById(R.id.fabSaveChanges).setOnClickListener(view -> onSaveTapped());
		findViewById(R.id.fabDiscardChanges).setOnClickListener(view -> onDiscardTapped());
		findViewById(R.id.fabDelete).setVisibility(GONE);

		((ScrollViewCategory<CourseViewModel>) findViewById(R.id.personalCoursesCategory)).setOnRemoveAction(course ->
				firebaseApi.getCourseService()
						.removeStudent(course, binding.getStudent())
						.onSuccess(none -> showSnackBar("Successfully removed yourself from the course", 1000))
						.onError(error -> logAndShowErrorSnack("An error occurred while removing yourself from the course",
								error,
								LOGGER)));

		((ScrollViewCategory<OtherActivityViewModel>) findViewById(R.id.personalActivitiesCategory)).setOnRemoveAction(activity ->
				firebaseApi.getOtherActivityService()
						.removeStudent(activity, binding.getStudent())
						.onSuccess(none -> showSnackBar("Successfully removed yourself from the activity", 1000))
						.onError(error -> logAndShowErrorSnack("An error occurred while removing yourself from the activity",
								error,
								LOGGER)));

		dispatcher.loadAll(personId);
		startUpdatingNextClass();
	}

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.student_activity_main_layout);
	}

	public void onEditTapped() {
		boolean editing = binding.getEditingProfile() == null ? false : binding.getEditingProfile();

		binding.setEditingProfile(!editing);
	}

	public void onSaveTapped() {
		hideKeyboard();

		if (dispatcher.update(binding.getStudent())) {
			binding.setEditingProfile(false);
		}
	}

	protected void onDiscardTapped() {
		hideKeyboard();

		if(binding.getStudent().equals(dispatcher.getCurrentProfile())){
			showSnackBar("No changes detected", 2000);
			return;
		}

		binding.setStudent(dispatcher.getCurrentProfile());
		binding.setEditingProfile(false);
	}

	protected ProfessorFileUploadDialog getFileUploadDialogInstance() {
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	protected void deleteFile(String professorKey, FileMetadataViewModel fileMetadata) {
	}

	protected void onDeleteTapped(Context context) {
	}

	protected ProfileImageUploadDialog getImageUploadDialog() {
		return new StudentImageUploadDialog(this, personId);
	}

	protected void deleteProfileImage() {
		firebaseApi.getStudentService()
				.deleteImage(personId, binding.getStudent().getImageMetadataKey())
				.onSuccess(none -> showSnackBar("Successfully deleted your profile image", 1000))
				.onError(error -> logAndShowErrorSnack("An error occurred while deleting your profile image",
						error,
						LOGGER));
	}

	protected void startUpdatingNextClass() {
		delayHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				updateNextClassTime.run();
				updateNextExamTime.run();

				delayHandler.postDelayed(this, CLASS_TIME_UPDATE_PERIOD);
			}
		}, CLASS_TIME_UPDATE_PERIOD);
	}

	protected void setNextClass(ScheduleClassViewModel nextClass) {
		binding.setNextScheduleClass(nextClass);
		Date dateOfNextClass = dateOfNextClass(nextClass);

		if (dateOfNextClass != null) {
			binding.setTimeToNextClass(dateOfNextClass.getTime() - new Date().getTime());
		}
	}

	protected void setNextExam(ScheduleClassViewModel nextExam) {
		binding.setNextExam(nextExam);
		Date dateOfNextExam = dateOfNextClass(nextExam);

		if (dateOfNextExam != null) {
			binding.setTimeToNextExam(dateOfNextExam.getTime() - new Date().getTime());
		}
	}

	private Runnable updateNextClassTime = () -> {
		if (binding.getNextScheduleClass() != null &&
				binding.getTimeToNextClass() != null) {
			long timeToNextClass = binding.getTimeToNextClass();

			timeToNextClass -= CLASS_TIME_UPDATE_PERIOD;

			if (timeToNextClass < 0) {
				binding.setNextScheduleClass(null);
			}
			else {
				binding.setTimeToNextClass(timeToNextClass);
			}
		}

		if (binding.getRecurringClasses() != null &&
				binding.getOneTimeClasses() != null &&
				binding.getNextScheduleClass() == null) {
			setNextClass(getNextClass(binding.getRecurringClasses().values(),
					binding.getOneTimeClasses().values()));
		}
	};

	private Runnable updateNextExamTime = () -> {
		if (binding.getNextExam() != null &&
				binding.getTimeToNextExam() != null) {
			long timeToNextExam = binding.getTimeToNextExam();

			timeToNextExam -= CLASS_TIME_UPDATE_PERIOD;

			if (timeToNextExam < 0) {
				binding.setNextExam(null);
			}
			else {
				binding.setTimeToNextExam(timeToNextExam);
			}
		}

		if (binding.getRecurringClasses() != null &&
				binding.getOneTimeClasses() != null &&
				binding.getNextExam() == null) {
			setNextExam(getNextExam(binding.getOneTimeClasses().values()));
		}
	};

	private static final int CLASS_TIME_UPDATE_PERIOD = 1000;
}
