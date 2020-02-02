package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.adapters.RecurringClassAdapter;
import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.database.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.entities.OtherActivity;
import com.lonn.studentassistant.firebaselayer.entities.RecurringClass;
import com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel;
import com.lonn.studentassistant.firebaselayer.entities.enums.WeekDay;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.services.abstractions.Service;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static androidx.databinding.library.baseAdapters.BR._all;
import static com.lonn.studentassistant.firebaselayer.Utils.calendarDayToWeekDay;
import static com.lonn.studentassistant.firebaselayer.database.DatabaseTableContainer.RECURRING_CLASSES;
import static com.lonn.studentassistant.firebaselayer.entities.enums.PermissionLevel.WRITE;
import static java.util.Calendar.DAY_OF_WEEK;

public class RecurringClassService extends Service<RecurringClass, Exception, RecurringClassViewModel> {
	private static RecurringClassService instance;
	private CourseService courseService;
	private OtherActivityService otherActivityService;
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
		super.init();
		adapter = new RecurringClassAdapter(firebaseConnection);
		courseService = CourseService.getInstance(firebaseConnection);
		professorService = ProfessorService.getInstance(firebaseConnection);
		otherActivityService = OtherActivityService.getInstance(firebaseConnection);
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
						otherActivityService.removeRecurringClass(recurringClass.getDiscipline(),
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
			otherActivityService.removeRecurringClass(recurringClass.getDiscipline(),
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
					otherActivityService.addRecurringClass(recurringClass.getDiscipline(), recurringClass.getKey());

					for (String professorKey : recurringClass.getProfessors()) {
						professorService.addRecurringClass(professorKey, recurringClass.getKey());
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<List<RecurringClassViewModel>, Exception> getByRoomAndDay(String room,
																			WeekDay weekDay) {
		Future<List<RecurringClassViewModel>, Exception> result = new Future<>();

		getAll().subscribe(false)
				.onComplete(classes -> {
					List<RecurringClassViewModel> returnedClasses = new LinkedList<>();

					for (RecurringClassViewModel recurringClass : classes) {
						if (recurringClass.getDayInt() == weekDay.getDayInt() &&
								recurringClass.getRooms().contains(room)) {
							returnedClasses.add(recurringClass);
						}
					}

					result.complete(returnedClasses);
				}, result::completeExceptionally);

		return result;
	}

	public Future<List<RecurringClassViewModel>, Exception> getByRoomAndDate(String room,
																			 Date date) {
		Future<List<RecurringClassViewModel>, Exception> result = new Future<>();

		getAll().subscribe(false)
				.onComplete(classes -> {
					List<RecurringClassViewModel> returnedClasses = new LinkedList<>();
					Calendar classDate = Calendar.getInstance();
					classDate.setTime(date);

					for (RecurringClassViewModel recurringClass : classes) {
						if (recurringClass.getDayInt() == calendarDayToWeekDay(calendarDayToWeekDay(classDate.get(DAY_OF_WEEK))) &&
								recurringClass.getRooms().contains(room)) {
							returnedClasses.add(recurringClass);
						}
					}

					result.complete(returnedClasses);
				}, result::completeExceptionally);

		return result;
	}

	@Override
	protected DatabaseTable<RecurringClass> getDatabaseTable() {
		return RECURRING_CLASSES;
	}

	protected PermissionLevel getPermissionLevel(RecurringClass recurringClass) {
		return WRITE;
//		return authenticationService.getPermissionLevel(recurringClass);
	}
}
