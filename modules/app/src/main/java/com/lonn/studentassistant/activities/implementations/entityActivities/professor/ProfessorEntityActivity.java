package com.lonn.studentassistant.activities.implementations.entityActivities.professor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FileManagingActivity;
import com.lonn.studentassistant.databinding.ProfessorEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations.ProfessorFileUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations.ProfessorImageUploadDialog;

import lombok.Getter;

import static com.lonn.studentassistant.firebaselayer.entities.enums.AccountType.STUDENT;
import static com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel.WRITE;

public class ProfessorEntityActivity extends FileManagingActivity<ProfessorViewModel> {
	private static final Logger LOGGER = Logger.ofClass(ProfessorEntityActivity.class);
	@Getter
	ProfessorEntityActivityLayoutBinding binding;
	private ProfessorEntityActivityFirebaseDispatcher dispatcher;
	private ProfessorImageUploadDialog imageUploadDialog;

	protected void loadAll(String entityKey) {
		dispatcher.loadAll(entityKey);
	}

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.professor_entity_activity_layout);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dispatcher = new ProfessorEntityActivityFirebaseDispatcher(this);
		imageUploadDialog = new ProfessorImageUploadDialog(this, entityKey);

		findViewById(R.id.imageUploadButton).setOnClickListener((v) -> imageUploadDialog.show());

		findViewById(R.id.imageDeleteButton).setOnClickListener((v) -> new AlertDialog.Builder(this, R.style.DialogTheme)
				.setTitle("Delete image")
				.setMessage("Are you sure you want to delete this image?\nThis action cannot be undone.")
				.setPositiveButton("Delete", (dialog, which) -> {
					deleteProfileImage();
					dialog.dismiss();
				})
				.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
				.create()
				.show());

		((ScrollViewCategory<CourseViewModel>) findViewById(R.id.coursesCategory))
				.setOnRemoveAction(this::showCourseRemoveDialog);
		((ScrollViewCategory<OtherActivityViewModel>) findViewById(R.id.activitiesCategory))
				.setOnRemoveAction(this::showActivityRemoveDialog);

		loadAll(entityKey);
	}

	private void showCourseRemoveDialog(CourseViewModel course) {
		new AlertDialog.Builder(this, R.style.DialogTheme)
				.setTitle("Removing course")
				.setMessage("Are you sure you wish to remove yourself from this course?")
				.setPositiveButton("Delete", (dialog, which) ->
						firebaseApi.getProfessorService()
								.removeCourse(binding.getEntity(), course.getKey())
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
								.removeActivity(binding.getEntity(), otherActivity.getKey())
								.onSuccess(none -> showSnackBar("Removed yourself from the activity", 1000))
								.onError(error -> logAndShowErrorSnack("An error occurred!",
										error,
										LOGGER)))
				.setNegativeButton("Cancel", null)
				.create()
				.show();
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

	protected ProfessorFileUploadDialog getFileUploadDialogInstance() {
		return new ProfessorFileUploadDialog(this, entityKey);
	}

	@Override
	protected void onEditTapped() {
		boolean editing = binding.getEditing() == null ? false : binding.getEditing();

		binding.setEditing(!editing);
		isEditing = !editing;
	}

	@Override
	protected void onDeleteTapped(Context context) {
		new AlertDialog.Builder(context, R.style.DialogTheme)
				.setTitle("Confirm deletion")
				.setMessage("Are you sure you want to delete this professor?")
				.setNegativeButton("Cancel", null)
				.setPositiveButton("Delete", (dialog, which) -> dispatcher.delete(binding.getEntity()))
				.create()
				.show();
	}

	@Override
	protected void onSaveTapped() {
		hideKeyboard();

		if (dispatcher.update(binding.getEntity())) {
			binding.setEditing(false);
		}
	}

	@Override
	protected void onDiscardTapped() {
		hideKeyboard();

		if (binding.getEntity().equals(dispatcher.getCurrentEntity())) {
			showSnackBar("No changes detected", 2000);
			binding.setEditing(false);
			return;
		}

		super.onDiscardTapped();
	}

	void updateBindingVariables() {
		if (firebaseApi.getAuthenticationService().getAccountType().equals(STUDENT)) {

			if (getBinding().getEntity() != null) {
				binding.setPermissionLevel(firebaseApi.getAuthenticationService()
						.getPermissionLevel(binding.getEntity()));
			}
		}
		else {
			binding.setPermissionLevel(firebaseApi.getAuthenticationService()
					.getPermissionLevel(binding.getEntity()));

			binding.setEditing(binding.getPermissionLevel().isAtLeast(WRITE) && binding.getEditing() != null && binding.getEditing() ?
					binding.getEditing()
					: false);

			isEditing = binding.getEditing();
		}
	}

	protected void deleteProfileImage() {
		firebaseApi.getProfessorService()
				.deleteImage(entityKey, binding.getEntity().getImageMetadataKey())
				.onSuccess(none -> showSnackBar("Successfully deleted your profile image", 1000))
				.onError(error -> logAndShowErrorSnack("An error occurred while deleting your profile image",
						error,
						LOGGER));
	}

	protected ProfessorViewModel getBindingEntity() {
		return getBinding().getEntity();
	}
}
