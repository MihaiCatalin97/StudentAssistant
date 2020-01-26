package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.RecurringClassAdapter;
import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;

import static androidx.databinding.library.baseAdapters.BR._all;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.RECURRING_CLASSES;

public class RecurringClassService extends Service<RecurringClass, Exception, RecurringClassViewModel> {
	private static RecurringClassService instance;
	private CourseService courseService;
	private ProfessorService professorService;

	private RecurringClassService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
	}

	public static RecurringClassService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new RecurringClassService(firebaseConnection);
			instance.init();
		}

		return instance;
	}

	protected void init() {
		adapter = new RecurringClassAdapter(firebaseConnection);
		courseService = CourseService.getInstance(firebaseConnection);
		professorService = ProfessorService.getInstance(firebaseConnection);
	}

	@Override
	protected RecurringClassViewModel transform(RecurringClass recurringClass) {
		RecurringClassViewModel result = super.transform(recurringClass);

		CourseService.getInstance(firebaseConnection)
				.getById(recurringClass.getDiscipline(), true)
				.onSuccess((discipline) -> {
					result.setDisciplineName(discipline.getName());
					result.notifyPropertyChanged(_all);
				});

		OtherActivityService.getInstance(firebaseConnection)
				.getById(recurringClass.getDiscipline(), true)
				.onSuccess((discipline) -> {
					result.setDisciplineName(discipline.getName());
					result.notifyPropertyChanged(_all);
				});

		return result;
	}

	@Override
	public Future<Void, Exception> deleteById(String recurringClassId) {
		Future<Void, Exception> result = new Future<>();

		getById(recurringClassId, false)
				.onSuccess(recurringClass -> {
					if (recurringClass == null) {
						result.completeExceptionally(new Exception("No class found"));
					}
					else {
						super.deleteById(recurringClassId)
								.onSuccess(result::complete)
								.onError(result::completeExceptionally);

						for (String professorKey : recurringClass.getProfessors()) {
							professorService.removeRecurringClass(professorKey, recurringClassId);
						}

						courseService.removeRecurringClass(recurringClass.getDiscipline(),
								recurringClassId);
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> delete(RecurringClassViewModel recurringClass) {
		Future<Void, Exception> result = new Future<>();

		if (recurringClass == null) {
			result.completeExceptionally(new Exception("No class found"));
		}
		else {
			super.deleteById(recurringClass.getKey())
					.onSuccess(result::complete)
					.onError(result::completeExceptionally);

			for (String professorKey : recurringClass.getProfessors()) {
				professorService.removeRecurringClass(professorKey, recurringClass.getKey());
			}

			courseService.removeRecurringClass(recurringClass.getDiscipline(),
					recurringClass.getKey());
		}

		return result;
	}

	@Override
	public Future<Void, Exception> save(RecurringClassViewModel recurringClass) {
		Future<Void, Exception> result = new Future<>();

		super.save(recurringClass)
				.onSuccess(none -> {
					result.complete(null);

					courseService.addRecurringClass(recurringClass.getDiscipline(), recurringClass.getKey());

					for (String professorKey : recurringClass.getProfessors()) {
						professorService.addRecurringClass(professorKey, recurringClass.getKey());
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	@Override
	protected DatabaseTable<RecurringClass> getDatabaseTable() {
		return RECURRING_CLASSES;
	}
}
