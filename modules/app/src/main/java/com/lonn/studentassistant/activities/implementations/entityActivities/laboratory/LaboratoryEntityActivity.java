package com.lonn.studentassistant.activities.implementations.entityActivities.laboratory;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FileManagingActivity;
import com.lonn.studentassistant.databinding.LaboratoryEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.GradeViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.LaboratoryViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.DialogBuilder;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.GradeInputDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.abstractions.FileUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations.LaboratoryFileUploadDialog;

import lombok.Getter;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.AccountType.STUDENT;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.GradeType.LABORATORY;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.PermissionLevel.WRITE;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
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
						.withTitle("Add grades")
						.withItems(asList("Add single grade", "Parse CSV"))
						.withItemActions(asList((item) -> gradeInputDialog.show(),
								(item) -> makeText(getBaseContext(), item, LENGTH_SHORT).show()))
						.show()
		);

		((ScrollViewCategory<GradeViewModel>) findViewById(R.id.gradesCategory)).setOnDeleteAction(grade ->
				firebaseApi.getGradeService()
						.deleteById(grade.getKey())
						.onSuccess(none -> showSnackBar("Successfully deleted grade!", 2000))
						.onError(error -> logAndShowErrorSnack("An error occured",
								error,
								LOGGER)));

		loadAll(entityKey);

		gradeInputDialog = new GradeInputDialog(this)
				.setAvailableGradeTypes(singletonList(LABORATORY))
				.setPositiveButtonAction((gradeParseResult) -> {
					try {
						if (gradeParseResult.getError() != null) {
							makeText(this, gradeParseResult.getError(), LENGTH_LONG).show();
							return;
						}

						gradeParseResult.getGrade()
								.setKey(randomUUID().toString())
								.setCourseKey(binding.getEntity().getCourseKey())
								.setLaboratoryKey(binding.getEntity().getKey())
								.setLaboratoryNumber(binding.getEntity().getWeekNumber());

						firebaseApi.getGradeService()
								.saveAndLink(gradeParseResult.getGrade())
								.onSuccess(none -> {
									showSnackBar("Successfully added grade!", 1000);
									gradeInputDialog.dismiss();
								})
								.onError(error -> logAndShowErrorToast("An error occurred while saving the grade", error, LOGGER));
					}
					catch (NumberFormatException exception) {
						logAndShowErrorToast("Invalid grade", exception, LOGGER);
					}
				});
	}

	protected void deleteFile(String laboratoryKey, FileMetadataViewModel fileMetadata) {
		getFirebaseApi().getLaboratoryService().deleteAndUnlinkFile(laboratoryKey, fileMetadata.getKey())
				.onSuccess(none -> showSnackBar("Successfully deleted " + fileMetadata.getFullFileName(), 1000))
				.onError(error -> logAndShowErrorSnack("An error occurred!", error, LOGGER));
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

	protected LaboratoryViewModel getBindingEntity(){
		return getBinding().getEntity();
	}
}
