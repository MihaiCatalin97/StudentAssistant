package com.lonn.studentassistant.activities.implementations.entityActivities.laboratory;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FileManagingActivity;
import com.lonn.studentassistant.databinding.LaboratoryEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.GradeViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.LaboratoryViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;
import com.lonn.studentassistant.views.implementations.dialog.DialogBuilder;
import com.lonn.studentassistant.views.implementations.dialog.GradeInputDialog;
import com.lonn.studentassistant.views.implementations.dialog.GradeInputDialogBuilder;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.abstractions.FileUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.implementations.LaboratoryFileUploadDialog;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;

public class LaboratoryEntityActivity extends FileManagingActivity<LaboratoryViewModel> {
	private static final Logger LOGGER = Logger.ofClass(LaboratoryEntityActivity.class);
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
								.setStudentKey(studentId)
								.setLaboratoryKey(binding.getLaboratory().getKey());
					}
					catch (NumberFormatException exception) {
						logAndShowErrorToast("Invalid grade", exception, LOGGER);
					}
				})
				.create();
	}

	protected void removeFileMetadataFromEntity(String laboratoryKey, String fileMetadataKey) {
		getFirebaseApi().getLaboratoryService()
				.getById(laboratoryKey)
				.subscribe(false)
				.onComplete(laboratory -> {
					laboratory.getFileMetadataKeys().remove(fileMetadataKey);

					getFirebaseApi().getLaboratoryService()
							.save(laboratory)
							.onCompleteDoNothing();
				});
	}

	protected FileUploadDialog getFileUploadDialogInstance() {
		return new LaboratoryFileUploadDialog(this, entityKey);
	}
}
