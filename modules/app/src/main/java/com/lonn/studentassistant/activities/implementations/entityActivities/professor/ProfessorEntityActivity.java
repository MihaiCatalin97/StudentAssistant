package com.lonn.studentassistant.activities.implementations.entityActivities.professor;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.EntityActivity;
import com.lonn.studentassistant.databinding.ProfessorEntityActivityLayoutBinding;
import com.lonn.studentassistant.viewModels.entities.ProfessorViewModel;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.FileUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.fileDialog.ProfessorImageUploadDialog;

import static android.content.Intent.createChooser;

public class ProfessorEntityActivity extends EntityActivity<ProfessorViewModel> {
	ProfessorEntityActivityLayoutBinding binding;
	private ProfessorViewModel viewModel;
	private ProfessorEntityActivityFirebaseDispatcher dispatcher;
	private FileUploadDialog fileUploadDialog;

	protected void loadAll() {
		dispatcher.loadAll();
	}

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.professor_entity_activity_layout);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		viewModel = getEntityFromIntent(this.getIntent());
		binding.setProfessor(viewModel);

		dispatcher = new ProfessorEntityActivityFirebaseDispatcher(this);

		findViewById(R.id.professorImageUploadButton).setOnClickListener((v) -> {
			Intent intent = new Intent()
					.setType("*/*")
					.setAction(Intent.ACTION_GET_CONTENT);

			startActivityForResult(createChooser(intent, "Select a file"),
					FileUploadDialog.SELECT_FILE_REQUEST_CODE);
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fileUploadDialog = new ProfessorImageUploadDialog(this, viewModel.getKey());

		fileUploadDialog.setFile(requestCode, resultCode, data);
		fileUploadDialog.readAndUpload();
	}
}
