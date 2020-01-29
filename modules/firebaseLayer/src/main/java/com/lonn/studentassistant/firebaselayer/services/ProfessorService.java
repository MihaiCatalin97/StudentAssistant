package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.ProfessorAdapter;
import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.viewModels.FileContentViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.FileMetadataViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.ProfessorViewModel;

import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.PROFESSORS;

public class ProfessorService extends FileAssociatedEntityService<Professor, Exception, ProfessorViewModel> {
	private static ProfessorService instance;

	private ProfessorService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
	}

	public static ProfessorService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new ProfessorService(firebaseConnection);
			instance.init();
		}

		return instance;
	}

	protected void init() {
		adapter = new ProfessorAdapter();
	}

	public Future<Void, Exception> addOrReplaceImage(String professorId,
													 FileMetadataViewModel fileMetadata,
													 FileContentViewModel fileContent) {
		Future<Void, Exception> result = new Future<>();

		fileMetadataService.saveMetadataAndContent(fileMetadata, fileContent)
				.onSuccess(none -> getById(professorId, false)
						.onSuccess(professor -> {
							String previousImage = professor.getImageMetadataKey();
							professor.setImageMetadataKey(fileMetadata.getKey());

							save(professor)
									.onSuccess(none2 -> {
										result.complete(none2);

										if (previousImage != null) {
											fileMetadataService.deleteMetadataAndContent(previousImage);
										}
									})
									.onError(result::completeExceptionally);
						})
						.onError(result::completeExceptionally))
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> addCourse(String professorKey, String courseKey) {
		Future<Void, Exception> result = new Future<>();

		getById(professorKey, false)
				.onSuccess(professor -> {
					if (professor == null) {
						result.completeExceptionally(new Exception("No professor found"));
					}
					else {
						if (!professor.getCourses().contains(courseKey)) {
							professor.getCourses().add(courseKey);
							save(professor)
									.onSuccess(result::complete)
									.onError(result::completeExceptionally);
						}
						else {
							result.complete(null);
						}
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> addRecurringClass(String professorKey, String recurringClassKey) {
		Future<Void, Exception> result = new Future<>();

		getById(professorKey, false)
				.onSuccess(professor -> {
					if (professor == null) {
						result.completeExceptionally(new Exception("No professor found"));
					}
					else {
						if (!professor.getRecurringClasses().contains(recurringClassKey)) {
							professor.getRecurringClasses().add(recurringClassKey);
							save(professor)
									.onSuccess(result::complete)
									.onError(result::completeExceptionally);
						}
						else {
							result.complete(null);
						}
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> addOneTimeClass(String professorKey, String oneTimeClassKey) {
		Future<Void, Exception> result = new Future<>();

		getById(professorKey, false)
				.onSuccess(professor -> {
					if (professor == null) {
						result.completeExceptionally(new Exception("No professor found"));
					}
					else {
						if (!professor.getOneTimeClasses().contains(oneTimeClassKey)) {
							professor.getOneTimeClasses().add(oneTimeClassKey);
							save(professor)
									.onSuccess(result::complete)
									.onError(result::completeExceptionally);
						}
						else {
							result.complete(null);
						}
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> addActivity(String professorKey, String activityKey) {
		Future<Void, Exception> result = new Future<>();

		getById(professorKey, false)
				.onSuccess(professor -> {
					if (professor == null) {
						result.completeExceptionally(new Exception("No professor found"));
					}
					else {
						if (!professor.getOtherActivities().contains(activityKey)) {
							professor.getOtherActivities().add(activityKey);
							save(professor)
									.onSuccess(result::complete)
									.onError(result::completeExceptionally);
						}
						else {
							result.complete(null);
						}
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> removeRecurringClass(String professorKey, String recurringClassKey) {
		Future<Void, Exception> result = new Future<>();

		getById(professorKey, false)
				.onSuccess(professor -> {
					if (professor == null) {
						result.completeExceptionally(new Exception("No professor found"));
					}
					else {
						if (professor.getRecurringClasses().contains(recurringClassKey)) {
							professor.getRecurringClasses().remove(recurringClassKey);
							save(professor)
									.onSuccess(result::complete)
									.onError(result::completeExceptionally);
						}
						else {
							result.complete(null);
						}
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> removeOneTimeClass(String professorKey, String oneTimeClassKey) {
		Future<Void, Exception> result = new Future<>();

		getById(professorKey, false)
				.onSuccess(professor -> {
					if (professor == null) {
						result.completeExceptionally(new Exception("No professor found"));
					}
					else {
						if (professor.getOneTimeClasses().contains(oneTimeClassKey)) {
							professor.getOneTimeClasses().remove(oneTimeClassKey);
							save(professor)
									.onSuccess(result::complete)
									.onError(result::completeExceptionally);
						}
						else {
							result.complete(null);
						}
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<ProfessorViewModel, Exception> getByName(String firstName, String lastName) {
		Future<ProfessorViewModel, Exception> result = new Future<>();

		getAll().onComplete(professors -> {
					for (ProfessorViewModel professor : professors) {
						if (professor.getFirstName().toLowerCase().equals(firstName.toLowerCase()) &&
								professor.getLastName().toLowerCase().equals(lastName.toLowerCase())) {
							result.complete(professor);
							return;
						}
					}

					result.complete(null);
				},
				result::completeExceptionally);

		return result;
	}

	@Override
	protected DatabaseTable<Professor> getDatabaseTable() {
		return PROFESSORS;
	}
}
