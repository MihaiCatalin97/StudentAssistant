package com.lonn.studentassistant.databinding;

import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecialization;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecializationYear;
import com.lonn.studentassistant.firebaselayer.entities.enums.WeekDay;
import com.lonn.studentassistant.firebaselayer.entities.enums.Year;
import com.lonn.studentassistant.firebaselayer.viewModels.CourseViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.OneTimeClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.RecurringClassViewModel;
import com.lonn.studentassistant.firebaselayer.viewModels.abstractions.EntityViewModel;
import com.lonn.studentassistant.viewModels.CategoryViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
		List<Date> categoriesAdded = new LinkedList<>();


		if (scheduleClasses != null) {
			List<T> scheduleClassesList = new ArrayList<>(scheduleClasses);
			sort(scheduleClassesList, (s1, s2) -> s1.getDate().compareTo(s2.getDate()));

			for (T scheduleClass : scheduleClassesList) {
				if (!categoriesAdded.contains(scheduleClass.getDate())) {
					subcategories.add(new CategoryViewModel<T>()
							.setCategoryTitle(simpleDateFormat.format(scheduleClass.getDate()))
							.setEntityName(parentCategory.getEntityName())
							.setViewType(parentCategory.getViewType())
							.setPermissionLevel(parentCategory.getPermissionLevel())
							.setShowEmpty(parentCategory.isShowEmpty())
							.setShowHeader(true)
							.setShouldContain((schClass) -> schClass.getDate().equals(scheduleClass.getDate())));

					categoriesAdded.add(scheduleClass.getDate());
				}
			}
		}

		return subcategories;
	}
}
