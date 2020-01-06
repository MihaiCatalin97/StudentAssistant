package com.lonn.studentassistant.activities.implementations.entityActivities.course;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.EntityActivity;
import com.lonn.studentassistant.databinding.CourseEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.FileContent;
import com.lonn.studentassistant.firebaselayer.entities.FileMetadata;
import com.lonn.studentassistant.firebaselayer.requests.DeleteByIdRequest;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;
import com.lonn.studentassistant.firebaselayer.requests.SaveRequest;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.utils.file.CustomFileReader;
import com.lonn.studentassistant.viewModels.entities.CourseViewModel;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;

import java.io.IOException;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.COURSES;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.FILE_CONTENT;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.FILE_METADATA;
import static com.lonn.studentassistant.firebaselayer.predicates.Predicate.where;
import static com.lonn.studentassistant.firebaselayer.predicates.fields.BaseEntityField.ID;

public class CourseEntityActivity extends EntityActivity<CourseViewModel> {
	CourseEntityActivityLayoutBinding binding;
	private CourseViewModel viewModel;
	private CourseEntityActivityFirebaseDispatcher dispatcher;
	private static final Logger LOGGER = Logger.ofClass(CourseEntityActivity.class);

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

		((ScrollViewCategory) findViewById(R.id.filesCategory)).setOnAddAction(() -> {
			Intent intent = new Intent()
					.setType("*/*")
					.setAction(Intent.ACTION_GET_CONTENT);

			startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		CustomFileReader customFileReader = new CustomFileReader(getContentResolver());
		if (requestCode == 123 && resultCode == RESULT_OK) {
			Uri fileUri = data.getData();
			try {
				FileMetadata fileMetadata = new FileMetadata()
						.setFileSize(customFileReader.getFileSize(fileUri))
						.setFileName(customFileReader.getFileName(fileUri))
						.setFileType(customFileReader.getFileType(fileUri));

				FileContent fileContent = new FileContent()
						.setFileContentBase64(customFileReader.readBase64(fileUri))
						.setFileMetadataKey(fileMetadata.getKey());

				fileMetadata.setFileContentKey(fileContent.getKey());

				saveFile(fileContent, fileMetadata);
			}
			catch (IOException exception) {
				logAndShowException("An error occurred while reading the file", exception);
			}
		}
	}

	private void saveFile(FileContent fileContent, FileMetadata fileMetadata) {
		getFirebaseConnection().execute(new SaveRequest<FileContent>()
				.entity(fileContent)
				.databaseTable(FILE_CONTENT)
				.onSuccess(() -> saveFileMetadata(fileMetadata))
				.onError((error) ->
						logAndShowException("An error occurred while uploading the file", error)
				));
	}

	private void saveFileMetadata(FileMetadata fileMetadata) {
		getFirebaseConnection().execute(new SaveRequest<FileMetadata>()
				.entity(fileMetadata)
				.databaseTable(FILE_METADATA)
				.onSuccess(() -> addMetadataToCourse(viewModel.getKey(), fileMetadata))
				.onError((error) -> {
					logAndShowException("An error occurred while uploading the file", error);
					deleteFileContent(fileMetadata.getFileContentKey());
				}));
	}

	private void deleteFileMetadata(String fileMetadataKey) {
		getFirebaseConnection().execute(new DeleteByIdRequest()
				.databaseTable(FILE_METADATA)
				.key(fileMetadataKey));
	}

	private void deleteFileContent(String fileContentKey) {
		getFirebaseConnection().execute(new DeleteByIdRequest()
				.databaseTable(FILE_CONTENT)
				.key(fileContentKey));
	}

	private void logAndShowException(String errorMessage, Exception exception) {
		showSnackBar(errorMessage);
		LOGGER.error(errorMessage, exception);
	}

	private void addMetadataToCourse(String courseKey, FileMetadata fileMetadata) {
		getFirebaseConnection().execute(new GetRequest<Course>()
				.databaseTable(COURSES)
				.predicate(where(ID).equalTo(courseKey))
				.onSuccess((courses) -> {
					Course course = courses.get(0);
					course.getFilesMetadata().add(fileMetadata.getKey());

					getFirebaseConnection().execute(new SaveRequest<Course>()
							.databaseTable(COURSES)
							.entity(course)
							.onSuccess(() -> showSnackBar("Successfully uploaded " + fileMetadata.getFileName() + "." +
									fileMetadata.getFileType()))
							.onError((exception) -> {
								logAndShowException("An error occurred while linking the file to the course",
										exception);

								deleteFileContent(fileMetadata.getFileContentKey());
								deleteFileMetadata(fileMetadata.getKey());
							}));
				})
				.subscribe(false));
	}
}
