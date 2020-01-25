package com.lonn.studentassistant.activities.implementations.entityActivities.laboratory;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FileManagingActivity;
import com.lonn.studentassistant.databinding.LaboratoryEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.GradeViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.LaboratoryViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.DialogBuilder;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.GradeInputDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.GradeInputDialogBuilder;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.abstractions.FileUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations.LaboratoryFileUploadDialog;

import java.util.Date;

import lombok.Getter;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;

public class LaboratoryEntityActivity extends FileManagingActivity<LaboratoryViewModel> {
	private static final Logger LOGGER = Logger.ofClass(LaboratoryEntityActivity.class);
	@Getter
	LaboratoryEntityActivityLayoutBinding binding;
	private LaboratoryEntityActivityFirebaseDispatcher dispatcher;
	private GradeInputDialog gradeInputDialog;

	protected void loadAll(String entityKey) {
		dispatcher.loadAll(entityKey);
	}

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.laboratory_entity_activity_layout);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dispatcher = new LaboratoryEntityActivityFirebaseDispatcher(this);

		((ScrollViewCategory) findViewById(R.id.gradesCategory)).setOnAddAction(() ->
				new DialogBuilder<String>(this)
						.withTitle("Add gradeKeys")
						.withItems(asList("Add single grade", "Parse CSV"))
						.withItemActions(asList((item) -> gradeInputDialog.show(),
								(item) -> makeText(getBaseContext(), item, LENGTH_SHORT).show()))
						.show()
		);

		loadAll(entityKey);


		gradeInputDialog = new GradeInputDialogBuilder(this)
				.positiveButtonAction(() -> {
					try {
						int grade = parseInt(gradeInputDialog.getGradeEditText().getText()
								.toString());
						String studentId = gradeInputDialog.getStudentIdEditText().getText()
								.toString();

						GradeViewModel gradeViewModel = new GradeViewModel()
								.setKey(randomUUID().toString())
								.setGrade(grade)
								.setStudentId(studentId)
								.setDate(new Date())
								.setLaboratoryKey(binding.getEntity().getKey());

						firebaseApi.getGradeService()
								.saveAndLink(gradeViewModel)
								.onSuccess(none -> {
									showSnackBar("Successfully added grade!", 1000);
									gradeInputDialog.dismiss();
								})
								.onError(error -> logAndShowErrorToast("An error occurred while saving the grade", error, LOGGER));
					}
					catch (NumberFormatException exception) {
						logAndShowErrorToast("Invalid grade", exception, LOGGER);
					}
				})
				.create();
	}

	protected void deleteFile(String laboratoryKey, FileMetadataViewModel fileMetadata) {
		getFirebaseApi().getLaboratoryService().deleteAndUnlinkFile(laboratoryKey, fileMetadata.getKey())
				.onSuccess(none -> showSnackBar("Successfully deleted " + fileMetadata.getFullFileName()))
				.onError(error -> logAndShowErrorSnack("An error occured!", error, LOGGER));
	}

	protected FileUploadDialog getFileUploadDialogInstance() {
		return new LaboratoryFileUploadDialog(this, entityKey);
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
				.setMessage("Are you sure you want to delete this laboratory?\n" +
						"This action will delete the grades and files associated to the laboratory.")
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
