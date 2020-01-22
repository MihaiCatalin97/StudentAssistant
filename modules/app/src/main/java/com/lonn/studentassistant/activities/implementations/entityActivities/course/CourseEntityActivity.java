package com.lonn.studentassistant.activities.implementations.entityActivities.course;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FileManagingActivity;
import com.lonn.studentassistant.activities.implementations.entityActivities.laboratory.LaboratoryInputActivity;
import com.lonn.studentassistant.databinding.CourseEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.abstractions.FileUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.implementations.CourseFileUploadDialog;

import lombok.Getter;

public class CourseEntityActivity extends FileManagingActivity<CourseViewModel> {
	private static final Logger LOGGER = Logger.ofClass(CourseEntityActivity.class);
	@Getter
	CourseEntityActivityLayoutBinding binding;
	private CourseEntityActivityFirebaseDispatcher dispatcher;

	protected void loadAll(String entityKey) {
		dispatcher.loadAll(entityKey);
	}

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.course_entity_activity_layout);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dispatcher = new CourseEntityActivityFirebaseDispatcher(this);

		((ScrollViewCategory) findViewById(R.id.laboratoriesCategory)).setOnAddAction(() -> {
			Intent laboratoryInputActivityIntent = new Intent(this,
					LaboratoryInputActivity.class);

			laboratoryInputActivityIntent.putExtra("courseName", binding.getEntity().getName());
			laboratoryInputActivityIntent.putExtra("courseKey", binding.getEntity().getKey());

			startActivity(laboratoryInputActivityIntent);
		});

		((ScrollViewCategory<StudentViewModel>) findViewById(R.id.studentCategory)).setOnRemoveAction((student) -> {
			if (activityEntity.getStudents().contains(student.getKey())) {
				new AlertDialog.Builder(getBaseContext(), R.style.DialogTheme)
						.setTitle("Remove student from course?")
						.setMessage("Are you sure you wish to remove this student from the course?")
						.setPositiveButton("Remove", (dialog, which) -> {
							activityEntity.getStudents().remove(student.getKey());

							firebaseApi.getCourseService()
									.save(activityEntity)
									.onError(error -> logAndShowErrorSnack("An error occurred!",
											error,
											LOGGER));

							student.getCourses().remove(entityKey);

							firebaseApi.getStudentService()
									.save(student)
									.onError(error -> logAndShowErrorSnack("An error occurred!",
											error,
											LOGGER));

							showSnackBar("Student removed from the course", 1000);
						})
						.setNegativeButton("Cancel", null)
						.create()
						.show();
			}
		});

		((ScrollViewCategory<FileMetadataViewModel>) findViewById(R.id.filesCategory)).setOnDeleteAction((file) -> {
			if (activityEntity.getFileMetadataKeys().contains(file.getKey())) {
				new AlertDialog.Builder(getBaseContext(), R.style.DialogTheme)
						.setTitle("File deletion")
						.setMessage("Are you sure you wish to delete this file?")
						.setPositiveButton("Delete", (dialog, which) -> {
							activityEntity.getFileMetadataKeys().remove(file.getKey());

							firebaseApi.getCourseService()
									.save(activityEntity)
									.onError(error -> logAndShowErrorSnack("An error occurred!",
											error,
											LOGGER));

							firebaseApi.getFileMetadataService()
									.deleteMetadataAndContent(file.getKey())
									.onError(error -> logAndShowErrorSnack("An error occurred!",
											error,
											LOGGER));

							showSnackBar("File deleted", 1000);
						})
						.setNegativeButton("Cancel", null)
						.create()
						.show();
			}
		});

		loadAll(entityKey);
	}

	protected void deleteFile(String courseKey, FileMetadataViewModel fileMetadata) {
		getFirebaseApi().getCourseService().deleteAndUnlinkFile(courseKey, fileMetadata.getKey())
				.onSuccess(none -> showSnackBar("Successfully deleted " + fileMetadata.getFullFileName()))
				.onError(error -> logAndShowErrorSnack("An error occured!", error, LOGGER));
	}

	protected FileUploadDialog getFileUploadDialogInstance() {
		return new CourseFileUploadDialog(this, entityKey);
	}

	@Override
	protected void onEditTapped() {
		boolean editing = binding.getEditing() == null ? false : binding.getEditing();

		binding.setEditing(!editing);
	}

	@Override
	protected void onDeleteTapped(Context context) {
		new AlertDialog.Builder(context, R.style.DialogTheme)
				.setTitle("Confirm deletion")
				.setMessage("Are you sure you want to delete this course?")
				.setNegativeButton("Cancel", null)
				.setPositiveButton("Delete", (dialog, which) -> dispatcher.delete(binding.getEntity()))
				.create()
				.show();

	}

	@Override
	protected void onSaveTapped() {
		dispatcher.update(binding.getEntity());
	}
}
