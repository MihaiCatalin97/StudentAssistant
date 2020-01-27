package com.lonn.studentassistant.firebaselayer.services;

import com.lonn.studentassistant.firebaselayer.api.Future;
import com.lonn.studentassistant.firebaselayer.entities.enums.WeekDay;
import com.lonn.studentassistant.firebaselayer.firebaseConnection.FirebaseConnection;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.ScheduleClassViewModel;

import java.util.Date;
import java.util.List;

public class RoomService {
	private static RoomService instance;
	private final FirebaseConnection firebaseConnection;
	private RecurringClassService recurringClassService;
	private OneTimeClassService oneTimeClassService;

	private RoomService(FirebaseConnection firebaseConnection) {
		this.firebaseConnection = firebaseConnection;
	}

	public static RoomService getInstance(FirebaseConnection firebaseConnection) {
		if (instance == null) {
			instance = new RoomService(firebaseConnection);
			instance.init();
		}

		return instance;
	}

	private void init() {
		recurringClassService = RecurringClassService.getInstance(firebaseConnection);
		oneTimeClassService = OneTimeClassService.getInstance(firebaseConnection);
	}

	public Future<Boolean, Exception> isOccupied(String room,
												 int startHour,
												 int endHour,
												 WeekDay day) {
		Future<Boolean, Exception> result = new Future<>();

		recurringClassService.getByRoomAndDay(room, day)
				.onSuccess(classes -> {
					boolean occupied = checkHoursOverlap(startHour, endHour, classes);

					if (occupied) {
						result.complete(true);
					}
					else {
						oneTimeClassService.getByRoomAndDay(room, day)
								.onSuccess(oneTimeClasses -> {
									boolean occupied2 = checkHoursOverlap(startHour, endHour, oneTimeClasses);

									result.complete(occupied2);
								})
								.onError(result::completeExceptionally);
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	public Future<Boolean, Exception> isOccupied(String room,
												 int startHour,
												 int endHour,
												 Date date) {
		Future<Boolean, Exception> result = new Future<>();

		recurringClassService.getByRoomAndDate(room, date)
				.onSuccess(classes -> {
					boolean occupied = checkHoursOverlap(startHour, endHour, classes);

					if (occupied) {
						result.complete(true);
					}
					else {
						oneTimeClassService.getByRoomAndDate(room, date)
								.onSuccess(oneTimeClasses -> {
									boolean occupied2 = checkHoursOverlap(startHour, endHour, oneTimeClasses);

									result.complete(occupied2);
								})
								.onError(result::completeExceptionally);
					}
				})
				.onError(result::completeExceptionally);

		return result;
	}

	private boolean checkHoursOverlap(int startHour, int endHour, List<? extends ScheduleClassViewModel> classes) {
		boolean occupied = false;

		for (ScheduleClassViewModel scheduleClass : classes) {
			if ((scheduleClass.getStartHourInt() > startHour && scheduleClass.getStartHourInt() < endHour) ||
					(scheduleClass.getEndHourInt() > startHour && scheduleClass.getEndHourInt() < endHour)) {
				occupied = true;
				break;
			}
		}

		return occupied;
	}
}
