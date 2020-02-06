package com.lonn.studentassistant.utils.schedule;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.ScheduleClassType;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.WeekDay;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.ScheduleClassViewModel;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lonn.studentassistant.firebaselayer.Utils.weekDayToCalendarWeekDay;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.ScheduleClassType.ARREARS_EXAM;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.ScheduleClassType.EXAM;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.ScheduleClassType.PARTIAL_EXAM;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.ScheduleClassType.PRACTICAL_EXAM;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.ScheduleClassType.PRACTICAL_TEST_EXAM;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.ScheduleClassType.PROJECTS_EXAM;
import static com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.WeekDay.getByInt;
import static java.util.Arrays.asList;

public class Utils {
	private static final List<ScheduleClassType> EXAM_TYPES = asList(EXAM,
			ARREARS_EXAM, PARTIAL_EXAM, PRACTICAL_EXAM, PRACTICAL_TEST_EXAM, PROJECTS_EXAM);

	public static ScheduleClassViewModel getNextClass(Collection<RecurringClassViewModel> recurringClasses,
													  Collection<OneTimeClassViewModel> oneTimeClasses) {
		Map<Date, ScheduleClassViewModel> classes = new HashMap<>();

		if (oneTimeClasses != null) {
			for (OneTimeClassViewModel oneTimeClass : oneTimeClasses) {
				if (!EXAM_TYPES.contains(oneTimeClass.type)) {
					Date nextDate = dateOfNextClass(oneTimeClass);

					if (nextDate != null) {
						classes.put(nextDate, oneTimeClass);
					}
				}
			}
		}

		if (recurringClasses != null) {
			for (RecurringClassViewModel recurringClass : recurringClasses) {
				if (!EXAM_TYPES.contains(recurringClass.type)) {
					Date nextDate = dateOfNextClass(recurringClass);

					if (nextDate != null) {
						classes.put(nextDate, recurringClass);
					}
				}
			}
		}

		Date smallestDate = null;

		for (Date date : classes.keySet()) {
			if (smallestDate == null || smallestDate.compareTo(date) >= 0) {
				smallestDate = date;
			}
		}

		if (smallestDate == null) {
			return null;
		}
		return classes.get(smallestDate);
	}

	public static ScheduleClassViewModel getNextExam(Collection<OneTimeClassViewModel> oneTimeClasses) {
		Map<Date, ScheduleClassViewModel> classes = new HashMap<>();

		if (oneTimeClasses != null) {
			for (OneTimeClassViewModel oneTimeClass : oneTimeClasses) {
				if (EXAM_TYPES.contains(oneTimeClass.type)) {
					Date nextDate = dateOfNextClass(oneTimeClass);

					if (nextDate != null) {
						classes.put(nextDate, oneTimeClass);
					}
				}
			}
		}

		Date smallestDate = null;

		for (Date date : classes.keySet()) {
			if (smallestDate == null || smallestDate.compareTo(date) >= 0) {
				smallestDate = date;
			}
		}

		if (smallestDate == null) {
			return null;
		}
		return classes.get(smallestDate);
	}

	public static Date dateOfNextClass(ScheduleClassViewModel scheduleClass) {
		if (scheduleClass instanceof RecurringClassViewModel) {
			return dateOfNextClass((RecurringClassViewModel) scheduleClass);
		}
		else if (scheduleClass instanceof OneTimeClassViewModel) {
			return dateOfNextClass((OneTimeClassViewModel) scheduleClass);
		}

		return null;
	}

	public static Date dateOfNextClass(OneTimeClassViewModel scheduleClass) {
		Calendar classDate = Calendar.getInstance();
		classDate.setTime(scheduleClass.getDate());

		classDate.set(Calendar.HOUR_OF_DAY, scheduleClass.startHour / 100);
		classDate.set(Calendar.MINUTE, scheduleClass.startHour % 100);
		classDate.set(Calendar.SECOND, 0);
		classDate.set(Calendar.MILLISECOND, 0);

		if (classDate.getTime().compareTo(new Date()) >= 0) {
			return classDate.getTime();
		}

		return null;
	}

	public static Date dateOfNextClass(RecurringClassViewModel scheduleClass) {
		Calendar classDate;
		WeekDay classDay = getByInt(scheduleClass.getDayInt());

		if (classDay != null) {
			classDate = getDateOfNextWeekDay(classDay);

			classDate.set(Calendar.HOUR_OF_DAY, scheduleClass.startHour / 100);
			classDate.set(Calendar.MINUTE, scheduleClass.startHour % 100);
			classDate.set(Calendar.SECOND, 0);
			classDate.set(Calendar.MILLISECOND, 0);

			if (classDate.getTime().compareTo(new Date()) >= 0) {
				return classDate.getTime();
			}
		}

		return null;
	}

	private static Calendar getDateOfNextWeekDay(WeekDay weekDay) {
		Calendar calendar = Calendar.getInstance();


		while (calendar.get(Calendar.DAY_OF_WEEK) != weekDayToCalendarWeekDay(weekDay)) {
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}

		return calendar;
	}

	public static String timeToDate(long millisecondsToDate) {
		long minutesToDate = millisecondsToDate / 1000 / 60;

		long days = minutesToDate / 60 / 24;
		long hours = minutesToDate / 60 % 24;
		long minutes = minutesToDate % 60;
		String result = "";

		if (days > 0) {
			result += days + " days";
		}
		if (hours > 0) {
			if (result.length() != 0) {
				result += ", ";
			}

			result += hours + " hours";
		}
		if (minutes > 0) {
			if (result.length() != 0) {
				result += ", ";
			}

			result += minutes + " minutes";
		}


		return result;
	}
}
