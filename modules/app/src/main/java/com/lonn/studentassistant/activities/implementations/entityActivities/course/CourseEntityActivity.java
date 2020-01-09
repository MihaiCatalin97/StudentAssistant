package com.lonn.studentassistant.activities.implementations.entityActivities.course;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.EntityActivity;
import com.lonn.studentassistant.databinding.CourseEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.CourseFileUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.FileUploadDialog;

public class CourseEntityActivity extends EntityActivity<CourseViewModel> {
	private static final Logger LOGGER = Logger.ofClass(CourseEntityActivity.class);
	CourseEntityActivityLayoutBinding binding;
	private CourseViewModel viewModel;
	private CourseEntityActivityFirebaseDispatcher dispatcher;
	private FileUploadDialog fileUploadDialog;

	protected void loadAll() {
		dispatcher.loadAll();
	}

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.course_entity_activity_layout);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		viewModel = getEntityFromIntent(this.getIntent());
		binding.setCourse(viewModel);

		dispatcher = new CourseEntityActivityFirebaseDispatcher(this);
		ScrollViewCategory<FileMetadataViewModel> filesCategory = findViewById(R.id.filesCategory);

		filesCategory.setOnAddAction(() -> {
			fileUploadDialog = new CourseFileUploadDialog(this,
					viewModel.getKey());
			fileUploadDialog.show();
		});

		filesCategory.setOnDeleteAction((FileMetadataViewModel fileViewModel) -> {
			firebaseApi.getFileMetadataService()
					.deleteById(fileViewModel.getKey());

			firebaseApi.getFileContentService()
					.deleteById(fileViewModel.getFileContentKey());

			removeMetadataFromCourse(viewModel.getKey(), fileViewModel.getKey());
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fileUploadDialog.setFile(requestCode, resultCode, data);
	}

	private void removeMetadataFromCourse(String courseKey, String fileMetadataKey) {
		getFirebaseApi().getCourseService()
				.getById(courseKey)
				.onComplete(course -> {
					course.getFilesMetadata().remove(fileMetadataKey);

					getFirebaseApi().getCourseService()
							.save(course);
				});
	}
}
