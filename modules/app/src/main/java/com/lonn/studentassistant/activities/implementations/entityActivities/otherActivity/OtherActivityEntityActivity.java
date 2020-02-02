package com.lonn.studentassistant.activities.implementations.entityActivities.otherActivity;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.FileManagingActivity;
import com.lonn.studentassistant.databinding.OtherActivityEntityActivityLayoutBinding;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OtherActivityViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.classes.OtherActivityOneTimeClassInputDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.classes.OtherActivityRecurringClassInputDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.abstractions.FileUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations.OtherActivityFileUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.selectionDialog.ProfessorSelectionDialog;
import com.lonn.studentassistant.views.implementations.dialog.selectionDialog.StudentSelectionDialog;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import static com.lonn.studentassistant.firebaselayer.entities.enums.AccountType.STUDENT;
import static com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel.WRITE;

public class OtherActivityEntityActivity extends FileManagingActivity<OtherActivityViewModel> {
	private static final Logger LOGGER = Logger.ofClass(OtherActivityEntityActivity.class);
	@Getter
	OtherActivityEntityActivityLayoutBinding binding;
	private OtherActivityFirebaseDispatcher dispatcher;

	protected void loadAll(String entityKey) {
		dispatcher.loadAll(entityKey);
	}

	protected void inflateLayout() {
		binding = DataBindingUtil.setContentView(this, R.layout.other_activity_entity_activity_layout);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dispatcher = new OtherActivityFirebaseDispatcher(this);

		((ScrollViewCategory) findViewById(R.id.studentCategory))
				.setOnAddAction(this::showStudentSelectionDialog);

		((ScrollViewCategory) findViewById(R.id.professorsCategory))
				.setOnAddAction(this::showProfessorSelectionDialog);

		((ScrollViewCategory) findViewById(R.id.recurringClassesCategory))
				.setOnAddAction(this::showRecurringClassInputDialog);

		((ScrollViewCategory) findViewById(R.id.oneTimeClassesCategory))
				.setOnAddAction(this::showOneTimeClassInputDialog);

		((ScrollViewCategory<StudentViewModel>) findViewById(R.id.studentCategory))
				.setOnRemoveAction(this::showStudentRemoveDialog);

		((ScrollViewCategory<FileMetadataViewModel>) findViewById(R.id.filesCategory))
				.setOnDeleteAction(this::showFileDeletionDialog);

		((ScrollViewCategory<ProfessorViewModel>) findViewById(R.id.professorsCategory))
				.setOnRemoveAction(this::showProfessorRemoveDialog);

		((ScrollViewCategory<RecurringClassViewModel>) findViewById(R.id.recurringClassesCategory))
				.setOnDeleteAction(this::showRecurringClassDeletionDialog);

		((ScrollViewCategory<OneTimeClassViewModel>) findViewById(R.id.oneTimeClassesCategory))
				.setOnDeleteAction(this::showOneTimeClassDeletionDialog);

		loadAll(entityKey);
	}

	protected void deleteFile(String activityKey, FileMetadataViewModel fileMetadata) {
		getFirebaseApi().getOtherActivityService().deleteAndUnlinkFile(activityKey, fileMetadata.getKey())
				.onSuccess(none -> showSnackBar("Successfully deleted " + fileMetadata.getFullFileName(), 1000))
				.onError(error -> logAndShowErrorSnack("An error occured!", error, LOGGER));
	}

	protected FileUploadDialog getFileUploadDialogInstance() {
		return new OtherActivityFileUploadDialog(this, entityKey);
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
				.setMessage("Are you sure you want to delete this activity?")
				.setNegativeButton("Cancel", null)
				.setPositiveButton("Delete", (dialog, which) -> dispatcher.delete(binding.getEntity()))
				.create()
				.show();
	}

	@Override
	protected void onSaveTapped() {
		dispatcher.update(binding.getEntity());
	}

	private void showStudentSelectionDialog() {
		showSnackBar("Loading students, please wait...", 1000);
		firebaseApi.getStudentService()
				.getAll()
				.subscribe(false)
				.onComplete(students -> {
					List<StudentViewModel> unEnrolledStudents = new ArrayList<>();

					for (StudentViewModel student : students) {
						if (!activityEntity.getStudents().contains(student.getKey())) {
							unEnrolledStudents.add(student);
						}
					}

					StudentSelectionDialog dialog = new StudentSelectionDialog(this);

					dialog.setTitle("Select students")
							.setInputHint("Enter student name or ID")
							.setItems(unEnrolledStudents)
							.setPositiveButtonAction(selectedStudents -> {
								getFirebaseApi().getOtherActivityService()
										.addStudents(selectedStudents, entityKey)
										.onSuccess(none -> showSnackBar("Successfully added students to the activity", 1500))
										.onError(error -> logAndShowErrorSnack("An error occurred while adding students to the activity",
												error,
												LOGGER));
							})
							.show();
				}, error -> logAndShowErrorSnack("An error occurred!",
						error,
						LOGGER));
	}

	private void showProfessorSelectionDialog() {
		showSnackBar("Loading professors, please wait...", 1000);
		firebaseApi.getProfessorService()
				.getAll()
				.subscribe(false)
				.onComplete(professors -> {
					List<ProfessorViewModel> unEnrolledProfessors = new ArrayList<>();

					for (ProfessorViewModel professor : professors) {
						if (!activityEntity.getProfessors().contains(professor.getKey())) {
							unEnrolledProfessors.add(professor);
						}
					}

					ProfessorSelectionDialog dialog = new ProfessorSelectionDialog(this);

					dialog.setTitle("Select professors")
							.setInputHint("Enter professor name")
							.setItems(unEnrolledProfessors)
							.setPositiveButtonAction(selectedProfessors -> {
								getFirebaseApi().getOtherActivityService()
										.addProfessors(selectedProfessors, entityKey)
										.onSuccess(none -> showSnackBar("Successfully added professors to the activity", 1500))
										.onError(error -> logAndShowErrorSnack("An error occurred while adding professors to the activity",
												error,
												LOGGER));
							})
							.show();
				}, error -> logAndShowErrorSnack("An error occurred!",
						error,
						LOGGER));
	}

	private void showRecurringClassInputDialog() {
		firebaseApi.getProfessorService()
				.getAll()
				.subscribe(false)
				.onComplete(professors -> {
					List<ProfessorViewModel> disciplineProfessors = new ArrayList<>();

					for (ProfessorViewModel professor : professors) {
						if (activityEntity.getProfessors().contains(professor.getKey())) {
							disciplineProfessors.add(professor);
						}
					}

					OtherActivityRecurringClassInputDialog dialog = new OtherActivityRecurringClassInputDialog(this,
							activityEntity,
							disciplineProfessors);

					dialog.setPositiveButtonAction((RecurringClassViewModel recurringClass) -> {
						firebaseApi.getRecurringClassService()
								.save(recurringClass)
								.onSuccess(none -> showSnackBar("Successfully created a new class!", 1000))
								.onError(error -> logAndShowErrorSnack("An error occurred while creating the new class",
										error,
										LOGGER));
					});
					dialog.show();
				}, error -> logAndShowErrorSnack("An error occurred!",
						error,
						LOGGER));
	}

	private void showOneTimeClassInputDialog() {
		firebaseApi.getProfessorService()
				.getAll()
				.subscribe(false)
				.onComplete(professors -> {
					List<ProfessorViewModel> disciplineProfessors = new ArrayList<>();

					for (ProfessorViewModel professor : professors) {
						if (activityEntity.getProfessors().contains(professor.getKey())) {
							disciplineProfessors.add(professor);
						}
					}

					OtherActivityOneTimeClassInputDialog dialog = new OtherActivityOneTimeClassInputDialog(this,
							activityEntity,
							disciplineProfessors);

					dialog.setPositiveButtonAction((OneTimeClassViewModel oneTimeClass) -> {
						firebaseApi.getOneTimeClassService()
								.save(oneTimeClass)
								.onSuccess(none -> showSnackBar("Successfully created a new class!", 1000))
								.onError(error -> logAndShowErrorSnack("An error occurred while creating the new class",
										error,
										LOGGER));
					});
					dialog.show();
				}, error -> logAndShowErrorSnack("An error occurred!",
						error,
						LOGGER));
	}

	private void showStudentRemoveDialog(StudentViewModel student) {
		new AlertDialog.Builder(this, R.style.DialogTheme)
				.setTitle("Remove student from activity?")
				.setMessage("Are you sure you wish to remove this student from the activity?")
				.setPositiveButton("Remove", (dialog, which) -> {
					firebaseApi.getOtherActivityService()
							.removeStudent(activityEntity, student)
							.onSuccess(none -> showSnackBar("Student removed from the activity", 1000))
							.onError(error -> logAndShowErrorSnack("An error occurred!",
									error,
									LOGGER));

				})
				.setNegativeButton("Cancel", null)
				.create()
				.show();
	}

	private void showProfessorRemoveDialog(ProfessorViewModel professor) {
		new AlertDialog.Builder(this, R.style.DialogTheme)
				.setTitle("Remove professor from activity?")
				.setMessage("Are you sure you wish to remove this professor from the activity?")
				.setPositiveButton("Remove", (dialog, which) -> {
					firebaseApi.getOtherActivityService()
							.removeProfessor(activityEntity, professor)
							.onSuccess(none -> showSnackBar("Professor removed from the activity", 1000))
							.onError(error -> logAndShowErrorSnack("An error occurred!",
									error,
									LOGGER));
				})
				.setNegativeButton("Cancel", null)
				.create()
				.show();
	}

	private void showFileDeletionDialog(FileMetadataViewModel file) {
		new AlertDialog.Builder(this, R.style.DialogTheme)
				.setTitle("File deletion")
				.setMessage("Are you sure you wish to delete this file?")
				.setPositiveButton("Delete", (dialog, which) -> {
					firebaseApi.getOtherActivityService()
							.deleteAndUnlinkFile(activityEntity, file.getKey())
							.onSuccess(none -> showSnackBar("File deleted", 1000))
							.onError(error -> logAndShowErrorSnack("An error occurred!",
									error,
									LOGGER));
				})
				.setNegativeButton("Cancel", null)
				.create()
				.show();
	}

	private void showRecurringClassDeletionDialog(RecurringClassViewModel recurringClass) {
		new AlertDialog.Builder(this, R.style.DialogTheme)
				.setTitle("Delete class from activity?")
				.setMessage("Are you sure you wish to delete this class from the activity?")
				.setPositiveButton("Delete", (dialog, which) -> {
					firebaseApi.getRecurringClassService()
							.delete(recurringClass)
							.onError(error -> logAndShowErrorSnack("An error occurred!",
									error,
									LOGGER));

					showSnackBar("Class deleted", 1000);
				})
				.setNegativeButton("Cancel", null)
				.create()
				.show();
	}

	private void showOneTimeClassDeletionDialog(OneTimeClassViewModel oneTimeClass) {
		new AlertDialog.Builder(this, R.style.DialogTheme)
				.setTitle("Delete class from activity?")
				.setMessage("Are you sure you wish to delete this class from the activity?")
				.setPositiveButton("Delete", (dialog, which) -> {
					activityEntity.getOneTimeClasses().remove(oneTimeClass.getKey());

					firebaseApi.getOtherActivityService()
							.save(activityEntity)
							.onError(error -> logAndShowErrorSnack("An error occurred!",
									error,
									LOGGER));

					firebaseApi.getOneTimeClassService()
							.deleteById(oneTimeClass.getKey())
							.onError(error -> logAndShowErrorSnack("An error occurred!",
									error,
									LOGGER));

					showSnackBar("Class deleted", 1000);
				})
				.setNegativeButton("Cancel", null)
				.create()
				.show();
	}

	void updateBindingVariables() {
		if (firebaseApi.getAuthenticationService().getAccountType().equals(STUDENT)) {
			binding.setUserIsStudent(true);

			if (getBinding().getEntity() != null) {
				String loggedPersonUUID = firebaseApi.getAuthenticationService()
						.getLoggedPersonUUID();

				binding.setEnrollRequestSent(getBinding()
						.getEntity()
						.getPendingStudents()
						.contains(loggedPersonUUID));

				binding.setEnrolled(getBinding()
						.getEntity()
						.getStudents()
						.contains(loggedPersonUUID));

				binding.setPermissionLevel(firebaseApi.getAuthenticationService()
						.getPermissionLevel(binding.getEntity()));
			}
		}
		else {
			binding.setUserIsStudent(false);
			binding.setEnrolled(false);
			binding.setEnrollRequestSent(false);
			binding.setPermissionLevel(firebaseApi.getAuthenticationService()
					.getPermissionLevel(binding.getEntity()));

			binding.setEditing(binding.getPermissionLevel().isAtLeast(WRITE) && binding.getEditing() != null && binding.getEditing() ?
					binding.getEditing()
					: false);

			isEditing = binding.getEditing();
		}
	}
}
