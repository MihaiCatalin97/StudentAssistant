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
import com.lonn.studentassistant.firebaselayer.viewModels.GradeViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.LaboratoryViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.logging.Logger;
import com.lonn.studentassistant.views.implementations.category.ScrollViewCategory;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.DialogBuilder;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.GradeInputDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.classes.CourseOneTimeClassInputDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.classes.CourseRecurringClassInputDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.abstractions.FileUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.inputDialog.file.implementations.CourseFileUploadDialog;
import com.lonn.studentassistant.views.implementations.dialog.selectionDialog.ProfessorSelectionDialog;
import com.lonn.studentassistant.views.implementations.dialog.selectionDialog.StudentSelectionDialog;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.lonn.studentassistant.firebaselayer.entities.enums.AccountType.STUDENT;
import static com.lonn.studentassistant.firebaselayer.entities.enums.GradeType.EXAM;
import static com.lonn.studentassistant.firebaselayer.entities.enums.GradeType.EXAM_ARREARS;
import static com.lonn.studentassistant.firebaselayer.entities.enums.GradeType.PARTIAL_ARREARS;
import static com.lonn.studentassistant.firebaselayer.entities.enums.GradeType.PARTIAL_EXAM;
import static com.lonn.studentassistant.firebaselayer.entities.enums.GradeType.PROJECT;
import static com.lonn.studentassistant.firebaselayer.entities.enums.GradeType.PROJECT_ARREARS;
import static com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel.WRITE;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;

public class CourseEntityActivity extends FileManagingActivity<CourseViewModel> {
	private static final Logger LOGGER = Logger.ofClass(CourseEntityActivity.class);

	@Getter
	CourseEntityActivityLayoutBinding binding;
	private CourseEntityActivityFirebaseDispatcher dispatcher;
	private GradeInputDialog gradeInputDialog;

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

		((ScrollViewCategory) findViewById(R.id.studentCategory))
				.setOnAddAction(this::showStudentSelectionDialog);

		((ScrollViewCategory) findViewById(R.id.professorsCategory))
				.setOnAddAction(this::showProfessorSelectionDialog);

		((ScrollViewCategory) findViewById(R.id.recurringClassesCategory))
				.setOnAddAction(this::showRecurringClassInputDialog);

		((ScrollViewCategory) findViewById(R.id.oneTimeClassesCategory))
				.setOnAddAction(this::showOneTimeClassInputDialog);
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


		((ScrollViewCategory<LaboratoryViewModel>) findViewById(R.id.laboratoriesCategory))
				.setOnDeleteAction(this::showLaboratoryDeletionDialog);

		((ScrollViewCategory<FileMetadataViewModel>) findViewById(R.id.filesCategory))
				.setOnDeleteAction(this::showFileDeletionDialog);

		((ScrollViewCategory<ProfessorViewModel>) findViewById(R.id.professorsCategory))
				.setOnRemoveAction(this::showProfessorRemoveDialog);

		((ScrollViewCategory<RecurringClassViewModel>) findViewById(R.id.recurringClassesCategory))
				.setOnDeleteAction(this::showRecurringClassDeletionDialog);

		((ScrollViewCategory<OneTimeClassViewModel>) findViewById(R.id.oneTimeClassesCategory))
				.setOnDeleteAction(this::showOneTimeClassDeletionDialog);

		((ScrollViewCategory<StudentViewModel>) findViewById(R.id.studentCategory)).setOnRemoveAction(student -> {
			if (binding.getEntity().getStudents().contains(student.getKey())) {
				this.showStudentRemoveDialog(student);
			}
			else {
				firebaseApi.getCourseService()
						.deleteEnrollmentRequest(binding.getEntity(), student.getKey())
						.onSuccess(none -> showSnackBar("Successfully removed enrollment request", 1000))
						.onError(error -> logAndShowErrorSnack("An error occurred!",
								error,
								LOGGER));
			}
		});

		((ScrollViewCategory<StudentViewModel>) findViewById(R.id.studentCategory)).setOnApproveAction(student -> {
			if (binding.getEntity().getPendingStudents().contains(student.getKey())) {
				firebaseApi.getCourseService()
						.approveEnrollmentRequest(binding.getEntity(), student.getKey())
						.onSuccess(none -> showSnackBar("Successfully approved enrollment request", 1000))
						.onError(error -> logAndShowErrorSnack("An error occurred!",
								error,
								LOGGER));
			}
		});

		findViewById(R.id.disciplineEnrollButton).setOnClickListener(view ->
				firebaseApi.getCourseService()
						.addEnrollmentRequest(getActivityEntity(), firebaseApi.getAuthenticationService().getLoggedPersonUUID())
						.onSuccess(none -> showSnackBar("Enroll request sent", 1000))
						.onError(error -> logAndShowErrorSnack("An error occurred!", error, LOGGER)));

		loadAll(entityKey);

		gradeInputDialog = new GradeInputDialog(this)
				.setAvailableGradeTypes(asList(EXAM, PARTIAL_EXAM, PROJECT, EXAM_ARREARS, PARTIAL_ARREARS, PROJECT_ARREARS))
				.setPositiveButtonAction((gradeParseResult) -> {
					if (gradeParseResult.getError() != null) {
						makeText(this, gradeParseResult.getError(), LENGTH_LONG).show();
						return;
					}

					gradeParseResult.getGrade()
							.setKey(randomUUID().toString())
							.setCourseKey(binding.getEntity().getKey());

					firebaseApi.getGradeService()
							.saveAndLink(gradeParseResult.getGrade())
							.onSuccess(none -> {
								showSnackBar("Successfully added grade!", 1000);
								gradeInputDialog.dismiss();
							})
							.onError(error -> logAndShowErrorToast("An error occurred while saving the grade", error, LOGGER));
				});
	}

	protected void deleteFile(String courseKey, FileMetadataViewModel fileMetadata) {
		getFirebaseApi().getCourseService().deleteAndUnlinkFile(courseKey, fileMetadata.getKey())
				.onSuccess(none -> showSnackBar("Successfully deleted " + fileMetadata.getFullFileName(), 1000))
				.onError(error -> logAndShowErrorSnack("An error occurred!", error, LOGGER));
	}

	protected FileUploadDialog getFileUploadDialogInstance() {
		return new CourseFileUploadDialog(this, entityKey);
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
								getFirebaseApi().getCourseService()
										.addStudents(selectedStudents, entityKey)
										.onSuccess(none -> showSnackBar("Successfully added students to the course", 1500))
										.onError(error -> logAndShowErrorSnack("An error occurred while adding students to the course",
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
								getFirebaseApi().getCourseService()
										.addProfessors(selectedProfessors, entityKey)
										.onSuccess(none -> showSnackBar("Successfully added professors to the course", 1500))
										.onError(error -> logAndShowErrorSnack("An error occurred while adding professors to the course",
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

					CourseRecurringClassInputDialog dialog = new CourseRecurringClassInputDialog(this,
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

					CourseOneTimeClassInputDialog dialog = new CourseOneTimeClassInputDialog(this,
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
				.setTitle("Remove student from course?")
				.setMessage("Are you sure you wish to remove this student from the course?")
				.setPositiveButton("Remove", (dialog, which) -> {
					firebaseApi.getCourseService()
							.removeStudent(activityEntity, student)
							.onSuccess(none -> showSnackBar("Student removed from the course", 1000))
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
				.setTitle("Remove professor from course?")
				.setMessage("Are you sure you wish to remove this professor from the course?")
				.setPositiveButton("Remove", (dialog, which) -> {
					firebaseApi.getCourseService()
							.removeProfessor(activityEntity, professor)
							.onSuccess(none -> showSnackBar("Professor removed from the course", 1000))
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
					firebaseApi.getCourseService()
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

	private void showLaboratoryDeletionDialog(LaboratoryViewModel laboratory) {
		new AlertDialog.Builder(this, R.style.DialogTheme)
				.setTitle("Delete laboratory?")
				.setMessage("Are you sure you wish to delete this laboratory?\n" +
						"This action will also delete the grades and files associated to this laboratory!")
				.setPositiveButton("Delete", (dialog, which) -> {
					firebaseApi.getLaboratoryService()
							.deleteById(laboratory.getKey())
							.onError(error -> logAndShowErrorSnack("An error occurred!",
									error,
									LOGGER));
					showSnackBar("Laboratory deleted", 1000);
				})
				.setNegativeButton("Cancel", null)
				.create()
				.show();
	}

	private void showRecurringClassDeletionDialog(RecurringClassViewModel recurringClass) {
		new AlertDialog.Builder(this, R.style.DialogTheme)
				.setTitle("Delete class from course?")
				.setMessage("Are you sure you wish to delete this class from the course?")
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
				.setTitle("Delete class from course?")
				.setMessage("Are you sure you wish to delete this class from the course?")
				.setPositiveButton("Delete", (dialog, which) -> {
					activityEntity.getOneTimeClasses().remove(oneTimeClass.getKey());

					firebaseApi.getCourseService()
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

	protected CourseViewModel getBindingEntity() {
		return getBinding().getEntity();
	}
}
