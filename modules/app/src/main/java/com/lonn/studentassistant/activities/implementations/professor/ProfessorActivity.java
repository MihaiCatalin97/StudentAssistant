package com.lonn.studentassistant.activities.implementations.professor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.Dispatcher;
import com.lonn.studentassistant.activities.abstractions.MainActivity;
import com.lonn.studentassistant.databinding.ProfessorActivityMainLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.AdministratorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.ScheduleClassViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.abstractions.ProfileImageUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations.ProfessorFileUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations.ProfessorImageUploadDialog;

import java.util.Date;

import lombok.Getter;

import static com.lonn.studentassistant.utils.schedule.Utils.dateOfNextClass;
import static com.lonn.studentassistant.utils.schedule.Utils.getNextClass;
import static com.lonn.studentassistant.utils.schedule.Utils.getNextExam;

public class ProfessorActivity extends MainActivity<ProfessorViewModel> {
	private static final Logger LOGGER = Logger.ofClass(ProfessorActivity.class);
	@Getter
	ProfessorActivityMainLayoutBinding binding;
	protected Dispatcher<ProfessorActivity, ProfessorViewModel> dispatcher;

	protected void loadAll(String entityKey) {
		dispatcher.loadAll(entityKey);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dispatcher = new ProfessorActivityFirebaseDispatcher(this);

		findViewById(R.id.fabEdit).setOnClickListener(view -> onEditTapped());
		findViewById(R.id.fabSaveChanges).setOnClickListener(view -> onSaveTapped());
		findViewById(R.id.fabDiscardChanges).setOnClickListener(view -> onDiscardTapped());

		((ScrollViewCategory<CourseViewModel>) findViewById(R.id.personalCoursesCategory))
				.setOnRemoveAction(this::showCourseRemoveDialog);
		((ScrollViewCategory<OtherActivityViewModel>) findViewById(R.id.personalActivitiesCategory))
				.setOnRemoveAction(this::showActivityRemoveDialog);

		loadAll(personId);
		startUpdatingNextClass();
	}

	@Override
	public void onBackPressed() {
		if (binding.getEditingProfile() != null &&
				binding.getEditingProfile()) {
			binding.setEditingProfile(false);
		}
		else {
			super.onBackPressed();
		}
	}

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.professor_activity_main_layout);
	}

	public void onEditTapped() {
		boolean editing = binding.getEditingProfile() == null ? false : binding.getEditingProfile();

		binding.setEditingProfile(!editing);
	}

	public void onSaveTapped() {
		hideKeyboard();

		if (dispatcher.update(binding.getProfessor())) {
			binding.setEditingProfile(false);
		}
	}

	private void showCourseRemoveDialog(CourseViewModel course) {
		new AlertDialog.Builder(this, R.style.DialogTheme)
				.setTitle("Removing course")
				.setMessage("Are you sure you wish to remove yourself from this course?")
				.setPositiveButton("Delete", (dialog, which) ->
						firebaseApi.getProfessorService()
								.removeCourse(binding.getProfessor(), course.getKey())
								.onSuccess(none -> showSnackBar("Removed yourself from the course", 1000))
								.onError(error -> logAndShowErrorSnack("An error occurred!",
										error,
										LOGGER)))
				.setNegativeButton("Cancel", null)
				.create()
				.show();
	}

	private void showActivityRemoveDialog(OtherActivityViewModel otherActivity) {
		new AlertDialog.Builder(this, R.style.DialogTheme)
				.setTitle("Removing activity")
				.setMessage("Are you sure you wish to remove yourself from this course?")
				.setPositiveButton("Delete", (dialog, which) ->
						firebaseApi.getProfessorService()
								.removeActivity(binding.getProfessor(), otherActivity.getKey())
								.onSuccess(none -> showSnackBar("Removed yourself from the activity", 1000))
								.onError(error -> logAndShowErrorSnack("An error occurred!",
										error,
										LOGGER)))
				.setNegativeButton("Cancel", null)
				.create()
				.show();
	}

	protected ProfessorFileUploadDialog getFileUploadDialogInstance() {
		return new ProfessorFileUploadDialog(this, entityKey);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		imageUploadDialog.setFile(requestCode, resultCode, data);
	}

	protected void deleteFile(String professorKey, FileMetadataViewModel fileMetadata) {
		getFirebaseApi().getProfessorService().deleteAndUnlinkFile(professorKey, fileMetadata.getKey())
				.onSuccess(none -> showSnackBar("Successfully deleted " + fileMetadata.getFullFileName(), 1000))
				.onError(error -> logAndShowErrorSnack("An error occurred!", error, LOGGER));
	}

	protected void onDeleteTapped(Context context) {
	}

	protected ProfileImageUploadDialog getImageUploadDialog() {
		return new ProfessorImageUploadDialog(this, personId);
	}

	protected void deleteProfileImage() {
		firebaseApi.getProfessorService()
				.deleteImage(personId, binding.getProfessor().getImageMetadataKey())
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
		if (binding.getNextScheduleClass() != null &&
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

	protected ProfessorViewModel getBindingEntity(){
		return getBinding().getProfessor();
	}
}
