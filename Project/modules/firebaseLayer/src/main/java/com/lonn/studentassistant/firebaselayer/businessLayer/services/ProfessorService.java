package com.lonn.studentassistant.firebaselayer.businessLayer.services;

import com.lonn.studentassistant.firebaselayer.businessLayer.adapters.ProfessorAdapter;
import com.lonn.studentassistant.firebaselayer.businessLayer.api.Future;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.PermissionLevel;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.businessLayer.services.abstractions.PersonService;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.ProfessorViewModel;

import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.database.DatabaseTableContainer.PROFESSORS;

public class ProfessorService extends PersonService<Professor, ProfessorViewModel> {
	private static ProfessorService instance;
	private static CourseService courseService;
	private static OtherActivityService otherActivityService;

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
		super.init();
		adapter = new ProfessorAdapter();
		courseService = CourseService.getInstance(firebaseConnection);
		otherActivityService = OtherActivityService.getInstance(firebaseConnection);
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

	public Future<Void, Exception> removeCourse(String professorKey, String courseKey) {
		Future<Void, Exception> result = new Future<>();

		getById(professorKey, false)
				.onSuccess(professor -> {
					if (professor == null) {
						result.completeExceptionally(new Exception("No professor found"));
					}
					else {
						if (professor.getCourses().contains(courseKey)) {
							professor.getCourses().remove(courseKey);
							save(professor)
									.onSuccess(result::complete)
									.onError(result::completeExceptionally);

							courseService.removeProfessor(courseKey, professor);
						}
						else {
							result.complete(null);
						}
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> removeActivity(String professorKey, String activityKey) {
		Future<Void, Exception> result = new Future<>();

		getById(professorKey, false)
				.onSuccess(professor -> {
					if (professor == null) {
						result.completeExceptionally(new Exception("No professor found"));
					}
					else {
						if (professor.getOtherActivities().contains(activityKey)) {
							professor.getOtherActivities().remove(activityKey);
							save(professor)
									.onSuccess(result::complete)
									.onError(result::completeExceptionally);

							otherActivityService.removeProfessor(activityKey, professor);
						}
						else {
							result.complete(null);
						}
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> removeCourse(ProfessorViewModel professor, String courseKey) {
		Future<Void, Exception> result = new Future<>();

		if (professor == null) {
			result.completeExceptionally(new Exception("No professor found"));
		}
		else {
			if (professor.getCourses().contains(courseKey)) {
				professor.getCourses().remove(courseKey);

				courseService.removeProfessor(courseKey, professor)
						.onSuccess(none -> save(professor)
								.onSuccess(result::complete)
								.onError(result::completeExceptionally))
						.onError(result::completeExceptionally);
			}
			else {
				result.complete(null);
			}
		}

		return result;
	}

	public Future<Void, Exception> removeActivity(ProfessorViewModel professor, String activityKey) {
		Future<Void, Exception> result = new Future<>();

		if (professor == null) {
			result.completeExceptionally(new Exception("No professor found"));
		}
		else {
			if (professor.getOtherActivities().contains(activityKey)) {
				professor.getOtherActivities().remove(activityKey);

				otherActivityService.removeProfessor(activityKey, professor)
						.onSuccess(none -> save(professor)
								.onSuccess(result::complete)
								.onError(result::completeExceptionally))
						.onError(result::completeExceptionally);
			}
			else {
				result.complete(null);
			}
		}

		return result;
	}

	public Future<ProfessorViewModel, Exception> getByName(String firstName, String lastName, boolean subscribe) {
		Future<ProfessorViewModel, Exception> result = new Future<>();

		getAll(subscribe)
				.onSuccess(professors -> {
					for (ProfessorViewModel professor : professors) {
						if (professor.getFirstName().toLowerCase().equals(firstName.toLowerCase()) &&
								professor.getLastName().toLowerCase().equals(lastName.toLowerCase())) {
							result.complete(professor);
							return;
						}
					}

					result.complete(null);
				})
				.onError(result::completeExceptionally);

		return result;
	}

	@Override
	protected DatabaseTable<Professor> getDatabaseTable() {
		return PROFESSORS;
	}

	protected PermissionLevel getPermissionLevel(Professor professor) {
		return authenticationService.getPermissionLevel(professor);
	}
}
