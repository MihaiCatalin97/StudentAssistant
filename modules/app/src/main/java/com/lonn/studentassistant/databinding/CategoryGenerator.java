package com.lonn.studentassistant.databinding;

import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.ScheduleClass;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecialization;
import com.lonn.studentassistant.firebaselayer.entities.enums.WeekDay;
import com.lonn.studentassistant.firebaselayer.entities.enums.Year;
import com.lonn.studentassistant.viewModels.CategoryViewModel;
import com.lonn.studentassistant.viewModels.entities.CourseViewModel;
import com.lonn.studentassistant.viewModels.entities.EntityViewModel;
import com.lonn.studentassistant.viewModels.entities.ScheduleClassViewModel;
import com.lonn.studentassistant.views.implementations.EntityView;

import java.util.ArrayList;
import java.util.List;

public class CategoryGenerator {
    public static <T extends Course, U extends EntityViewModel<T>> List<CategoryViewModel<T, U>> studyCycleCategories(CategoryViewModel<T, U> parentCategory) {
        List<CategoryViewModel<T, U>> subcategories = new ArrayList<>();

        for (CycleSpecialization cycleSpecialization : CycleSpecialization.values()) {
            subcategories.add(new CategoryViewModel<T, U>()
                    .setCategoryTitle(cycleSpecialization.toString())
                    .setEntityName(parentCategory.getEntityName())
                    .setViewType(parentCategory.getViewType())
                    .setPermissionLevel(parentCategory.getPermissionLevel())
                    .setShowEmpty(parentCategory.isShowEmpty())
                    .setShowHeader(true)
                    .setShouldContain((course) -> ((CourseViewModel) course).cycleSpecialization
                            .equals(cycleSpecialization))
                    .setChildCategories(yearCategories(parentCategory,
                            cycleSpecialization.getCycle()
                                    .getNumberOfYears())));
        }

        return subcategories;
    }

    public static <T extends Course, U extends EntityViewModel<T>> List<CategoryViewModel<T, U>> yearCategories(CategoryViewModel<T, U> parentCategory,
                                                                                                             int numberOfYears) {
        List<CategoryViewModel<T, U>> subcategories = new ArrayList<>();

        for (int yearIndex = 1; yearIndex <= numberOfYears; yearIndex++) {
            String yearString = Year.valueOf(yearIndex).getYearString() + " year";
            final int year = yearIndex;

            subcategories.add(new CategoryViewModel<T, U>()
                    .setCategoryTitle(yearString)
                    .setEntityName(parentCategory.getEntityName())
                    .setViewType(parentCategory.getViewType())
                    .setPermissionLevel(parentCategory.getPermissionLevel())
                    .setShowEmpty(parentCategory.isShowEmpty())
                    .setShowHeader(true)
                    .setShouldContain((discipline) -> ((CourseViewModel)discipline).getYear() == year));
        }

        return subcategories;
    }

    public static <T extends ScheduleClass, U extends EntityViewModel<T>> List<CategoryViewModel<T, U>> scheduleCategories(CategoryViewModel<T, U> parentCategory) {
        List<CategoryViewModel<T, U>> subcategories = new ArrayList<>();

        for (WeekDay weekDay : WeekDay.values()) {
            subcategories.add(new CategoryViewModel<T, U>()
                    .setCategoryTitle(weekDay.getDayStringEng())
                    .setEntityName(parentCategory.getEntityName())
                    .setViewType(parentCategory.getViewType())
                    .setPermissionLevel(parentCategory.getPermissionLevel())
                    .setShowEmpty(parentCategory.isShowEmpty())
                    .setShowHeader(true)
                    .setShouldContain((scheduleClass) -> scheduleClass instanceof ScheduleClassViewModel &&
                            ((ScheduleClassViewModel) scheduleClass).day == weekDay.getDayInt()));
        }

        return subcategories;
    }
}
