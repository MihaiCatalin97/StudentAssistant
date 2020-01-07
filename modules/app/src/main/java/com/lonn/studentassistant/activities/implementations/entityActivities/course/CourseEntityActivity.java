package com.lonn.studentassistant.activities.implementations.entityActivities.course;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.EntityActivity;
import com.lonn.studentassistant.databinding.CourseEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.requests.DeleteByIdRequest;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.requests.SaveRequest;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.viewModels.entities.CourseViewModel;
import com.lonn.studentassistant.viewModels.entities.FileViewModel;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.CourseFileUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.FileUploadDialog;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.COURSES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.FILE_CONTENT;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.FILE_METADATA;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.BaseEntityField.ID;

public class CourseEntityActivity extends EntityActivity<CourseViewModel> {
	private static final Logger LOGGER = Logger.ofClass(CourseEntityActivity.class);
	CourseEntityActivityLayoutBinding binding;
	private CourseViewModel viewModel;
	private CourseEntityActivityFirebaseDispatcher dispatcher;
	private FileUploadDialog fileUploadDialog;
	private ScrollViewCategory<FileViewModel> filesCategory;

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
		filesCategory = findViewById(R.id.filesCategory);

		filesCategory.setOnAddAction(() -> {
			fileUploadDialog = new CourseFileUploadDialog(this,
					viewModel.getKey());
			fileUploadDialog.show();
		});

		filesCategory.setOnDeleteAction((FileViewModel fileViewModel) -> {
			firebaseConnection.execute(new DeleteByIdRequest()
					.databaseTable(FILE_METADATA)
					.key(fileViewModel.getKey()));

			firebaseConnection.execute(new DeleteByIdRequest()
					.databaseTable(FILE_CONTENT)
					.key(fileViewModel.getFileContentKey()));

			removeMetadataFromCourse(viewModel.getKey(), fileViewModel.getKey());
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fileUploadDialog.setFile(requestCode, resultCode, data);
	}

	private void removeMetadataFromCourse(String courseKey, String fileMetadataKey) {
		getFirebaseConnection().execute(new GetRequest<Course>()
				.databaseTable(COURSES)
				.predicate(where(ID).equalTo(courseKey))
				.onSuccess((courses) -> {
					Course course = courses.get(0);
					course.getFilesMetadata().remove(fileMetadataKey);

					getFirebaseConnection().execute(new SaveRequest<Course>()
							.databaseTable(COURSES)
							.entity(course));
				})
				.subscribe(false));
	}
}
