package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.OneTimeClassAdapter;
import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.OneTimeClass;
import com.lonn.studentassistant.firebaselayer.entities.enums.WeekDay;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.services.abstractions.Service;
import com.lonn.studentassistant.firebaselayer.viewModels.OneTimeClassViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static androidx.databinding.library.baseAdapters.BR._all;
import static com.lonn.studentassistant.firebaselayer.Utils.weekDayToCalendarWeekDay;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.ONE_TIME_CLASSES;
import static java.util.Calendar.DAY_OF_WEEK;

public class OneTimeClassService extends Service<OneTimeClass, Exception, OneTimeClassViewModel> {
	private static OneTimeClassService instance;
	private CourseService courseService;
	private ProfessorService professorService;

	private OneTimeClassService(FirebaseConnection firebaseConnection) {
		super(firebaseConnection);
		adapter = new OneTimeClassAdapter(firebaseConnection);
	}

	public static OneTimeClassService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new OneTimeClassService(firebaseConnection);
			instance.init();
		}

		return instance;
	}

	protected void init() {
		adapter = new OneTimeClassAdapter(firebaseConnection);
		courseService = CourseService.getInstance(firebaseConnection);
		professorService = ProfessorService.getInstance(firebaseConnection);
	}

	@Override
	protected OneTimeClassViewModel transform(OneTimeClass oneTimeClass) {
		OneTimeClassViewModel result = super.transform(oneTimeClass);

		CourseService.getInstance(firebaseConnection)
				.getById(oneTimeClass.getDiscipline(), true)
				.onSuccess((discipline) -> {
					result.setDisciplineName(discipline.getName());
					result.notifyPropertyChanged(_all);
				});

		OtherActivityService.getInstance(firebaseConnection)
				.getById(oneTimeClass.getDiscipline(), true)
				.onSuccess((discipline) -> {
					result.setDisciplineName(discipline.getName());
					result.notifyPropertyChanged(_all);
				});

		return result;
	}

	@Override
	public Future<Void, Exception> deleteById(String oneTimeClassId) {
		Future<Void, Exception> result = new Future<>();

		getById(oneTimeClassId, false)
				.onSuccess(oneTimeClass -> {
					if (oneTimeClass == null) {
						result.completeExceptionally(new Exception("No class found"));
					}
					else {
						super.deleteById(oneTimeClassId)
								.onSuccess(result::complete)
								.onError(result::completeExceptionally);

						for (String professorKey : oneTimeClass.getProfessors()) {
							professorService.removeOneTimeClass(professorKey, oneTimeClassId);
						}

						courseService.removeOneTimeClass(oneTimeClass.getDiscipline(),
								oneTimeClassId);
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Void, Exception> delete(OneTimeClass oneTimeClass) {
		Future<Void, Exception> result = new Future<>();

		if (oneTimeClass == null) {
			result.completeExceptionally(new Exception("No class found"));
		}
		else {
			super.deleteById(oneTimeClass.getKey())
					.onSuccess(result::complete)
					.onError(result::completeExceptionally);

			for (String professorKey : oneTimeClass.getProfessors()) {
				professorService.removeRecurringClass(professorKey, oneTimeClass.getKey());
			}

			courseService.removeRecurringClass(oneTimeClass.getDiscipline(),
					oneTimeClass.getKey());
		}

		return result;
	}

	public Future<List<OneTimeClassViewModel>, Exception> getByRoomAndDay(String room,
																		  WeekDay day) {
		Future<List<OneTimeClassViewModel>, Exception> result = new Future<>();

		getAll().subscribe(false)
				.onComplete(classes -> {
					List<OneTimeClassViewModel> returnedClasses = new LinkedList<>();

					for (OneTimeClassViewModel oneTimeClass : classes) {
						Calendar classDate = Calendar.getInstance();
						classDate.setTime(oneTimeClass.getDate());

						if (classDate.get(DAY_OF_WEEK) == weekDayToCalendarWeekDay(day) &&
								oneTimeClass.getRooms().contains(room)) {
							returnedClasses.add(oneTimeClass);
						}
					}

					result.complete(returnedClasses);
				}, result::completeExceptionally);

		return result;
	}

	public Future<List<OneTimeClassViewModel>, Exception> getByRoomAndDate(String room,
																		   Date date) {
		Future<List<OneTimeClassViewModel>, Exception> result = new Future<>();

		getAll().subscribe(false)
				.onComplete(classes -> {
					List<OneTimeClassViewModel> returnedClasses = new LinkedList<>();

					for (OneTimeClassViewModel oneTimeClass : classes) {
						if (oneTimeClass.getDate().equals(date) &&
								oneTimeClass.getRooms().contains(room)) {
							returnedClasses.add(oneTimeClass);
						}
					}

					result.complete(returnedClasses);
				}, result::completeExceptionally);

		return result;
	}

	@Override
	public Future<Void, Exception> save(OneTimeClassViewModel oneTimeClass) {
		Future<Void, Exception> result = new Future<>();

		super.save(oneTimeClass)
				.onSuccess(none -> {
					result.complete(null);

					courseService.addOneTimeClass(oneTimeClass.getDiscipline(), oneTimeClass.getKey());

					for (String professorKey : oneTimeClass.getProfessors()) {
						professorService.addOneTimeClass(professorKey, oneTimeClass.getKey());
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	@Override
	protected DatabaseTable<OneTimeClass> getDatabaseTable() {
		return ONE_TIME_CLASSES;
	}
}
