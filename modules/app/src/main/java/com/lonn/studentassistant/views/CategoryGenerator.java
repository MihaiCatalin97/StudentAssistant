package com.lonn.studentassistant.views;

import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.Professor;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecialization;
import com.lonn.studentassistant.firebaselayer.entities.enums.Year;
import com.lonn.studentassistant.viewModels.entities.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryGenerator {
    public static <T extends Course> List<CategoryViewModel<T>> studyCycleCategories(CategoryViewModel<T> parentCategory) {
        List<CategoryViewModel<T>> subcategories = new ArrayList<>();

        for (CycleSpecialization cycleSpecialization : CycleSpecialization.values()) {
            subcategories.add(new CategoryViewModel<T>()
                    .setCategoryTitle(cycleSpecialization.toString())
                    .setEntityName(parentCategory.getEntityName())
                    .setViewType(parentCategory.getViewType())
                    .setPermissionLevel(parentCategory.getPermissionLevel())
                    .setShowEmpty(parentCategory.isShowEmpty())
                    .setShowHeader(true)
                    .setShouldContain((discipline) ->
                            discipline.getCycleAndSpecialization()
                                    .equals(cycleSpecialization))
                    .setChildCategories(yearCategories(parentCategory,
                            cycleSpecialization.getCycle()
                                    .getNumberOfYears())));
        }

        return subcategories;
    }

    public static <T extends Course> List<CategoryViewModel<T>> yearCategories(CategoryViewModel<T> parentCategory,
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
                    .setShouldContain((discipline) -> discipline.getYear() == year));
        }

        return subcategories;
    }
}
