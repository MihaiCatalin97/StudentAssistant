package com.lonn.studentassistant.databinding;

import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.CycleSpecialization;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.CycleSpecializationYear;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.WeekDay;
import com.lonn.studentassistant.firebaselayer.dataAccessLayer.entities.enums.Year;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.StudentViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.DisciplineViewModel;
import com.lonn.studentassistant.firebaselayer.businessLayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.viewModels.CategoryViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static java.util.Calendar.DAY_OF_YEAR;
import static java.util.Calendar.YEAR;
import static java.util.Collections.sort;

public class CategoryGenerator {
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM YYYY (EEEE)");

	public static <T extends EntityViewModel<? extends Course>> List<CategoryViewModel<T>> studyCycleCategories(CategoryViewModel<T> parentCategory) {
		List<CategoryViewModel<T>> subcategories = new ArrayList<>();

		for (CycleSpecialization cycleSpecialization : CycleSpecialization.values()) {
			subcategories.add(new CategoryViewModel<T>()
					.setCategoryTitle(cycleSpecialization.toString())
					.setEntityName(parentCategory.getEntityName())
					.setViewType(parentCategory.getViewType())
					.setPermissionLevel(parentCategory.getPermissionLevel())
					.setShowEmpty(parentCategory.isShowEmpty())
					.setShowHeader(true)
					.setShouldContain((course) -> {
						for (CycleSpecializationYear cycleSpecializationYear : ((CourseViewModel) course).getCycleSpecializationYears()) {
							if (cycleSpecializationYear.getCycleSpecialization().equals(cycleSpecialization))
								return true;
						}

						return false;
					})
					.setChildCategories(yearCategories(parentCategory,
							cycleSpecialization.getCycle()
									.getNumberOfYears())));
		}

		return subcategories;
	}

	public static <T extends EntityViewModel<? extends Course>> List<CategoryViewModel<T>> yearCategories(CategoryViewModel<T> parentCategory,
																										  int numberOfYears) {
		List<CategoryViewModel<T>> subcategories = new ArrayList<>();

		for (int yearIndex = 1; yearIndex <= numberOfYears; yearIndex++) {
			String yearString = Year.valueOf(yearIndex).getYearString() + " year";
			final int year = yearIndex;

			subcategories.add(new CategoryViewModel<T>()
					.setCategoryTitle(yearString)
					.setEntityName(parentCategory.getEntityName())
					.setViewType(parentCategory.getViewType())
					.setPermissionLevel(parentCategory.getPermissionLevel())
					.setShowEmpty(parentCategory.isShowEmpty())
					.setShowHeader(true)
					.setShouldContain((discipline) -> {
						for (CycleSpecializationYear cycleSpecializationYear : ((CourseViewModel) discipline).getCycleSpecializationYears()) {
							if (cycleSpecializationYear.getYear() == year)
								return true;
						}

						return false;
					}));
		}

		return subcategories;
	}

	public static <T extends RecurringClassViewModel> List<CategoryViewModel<T>> scheduleCategories(CategoryViewModel<T> parentCategory) {
		List<CategoryViewModel<T>> subcategories = new ArrayList<>();

		for (WeekDay weekDay : WeekDay.values()) {
			subcategories.add(new CategoryViewModel<T>()
					.setCategoryTitle(weekDay.getDayStringEng())
					.setEntityName(parentCategory.getEntityName())
					.setViewType(parentCategory.getViewType())
					.setPermissionLevel(parentCategory.getPermissionLevel())
					.setShowEmpty(parentCategory.isShowEmpty())
					.setShowHeader(true)
					.setShouldContain((scheduleClass) -> scheduleClass.getDayInt() == weekDay.getDayInt()));
		}

		return subcategories;
	}

	public static <T extends OneTimeClassViewModel> List<CategoryViewModel<T>> oneTimeScheduleCategories(CategoryViewModel<T> parentCategory,
																										 Collection<T> scheduleClasses) {
		List<CategoryViewModel<T>> subcategories = new ArrayList<>();
		List<String> categoriesAdded = new LinkedList<>();

		if (scheduleClasses != null) {
			List<T> scheduleClassesList = new ArrayList<>(scheduleClasses);
			sort(scheduleClassesList, (s1, s2) -> s1.getDate().compareTo(s2.getDate()));

			for (T scheduleClass : scheduleClassesList) {
				String dateFormatted = simpleDateFormat.format(scheduleClass.getDate());
				if (!categoriesAdded.contains(dateFormatted)) {
					subcategories.add(new CategoryViewModel<T>()
							.setCategoryTitle(dateFormatted)
							.setEntityName(parentCategory.getEntityName())
							.setViewType(parentCategory.getViewType())
							.setPermissionLevel(parentCategory.getPermissionLevel())
							.setShowEmpty(parentCategory.isShowEmpty())
							.setShowHeader(true)
							.setShouldContain((schClass) -> {
								Calendar c1 = Calendar.getInstance();
								Calendar c2 = Calendar.getInstance();

								c1.setTime(schClass.getDate());
								c2.setTime(scheduleClass.getDate());

								return c1.get(YEAR) == c2.get(YEAR) && c1.get(DAY_OF_YEAR) == c2.get(DAY_OF_YEAR);
							}));

					categoriesAdded.add(dateFormatted);
				}
			}
		}

		return subcategories;
	}

	public static <T extends StudentViewModel> List<CategoryViewModel<T>> courseStudentCategories(CategoryViewModel<T> parentCategory,
																								  DisciplineViewModel courseViewModel) {
		List<CategoryViewModel<T>> subcategories = new ArrayList<>();

		subcategories.add(new CategoryViewModel<T>()
				.setCategoryTitle("Approved students")
				.setEntityName(parentCategory.getEntityName())
				.setViewType(parentCategory.getViewType())
				.setPermissionLevel(parentCategory.getPermissionLevel())
				.setShowEmpty(parentCategory.isShowEmpty())
				.setShowHeader(true)
				.setOnAdd(parentCategory.getOnAdd())
				.setShouldContain((student) -> {
					if (courseViewModel == null) {
						return true;
					}

					return courseViewModel.getStudents().contains(student.getKey());
				}));

		subcategories.add(new CategoryViewModel<T>()
				.setCategoryTitle("Pending students")
				.setEntityName(parentCategory.getEntityName())
				.setViewType(parentCategory.getViewType())
				.setPermissionLevel(parentCategory.getPermissionLevel())
				.setShowEmpty(parentCategory.isShowEmpty())
				.setShowHeader(true)
				.setCanApprove(true)
				.setShouldContain((student) -> {
					if (courseViewModel == null) {
						return false;
					}

					return courseViewModel.getPendingStudents().contains(student.getKey());
				}));

		return subcategories;
	}
}
