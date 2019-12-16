package com.lonn.studentassistant.views;

import com.lonn.studentassistant.firebaselayer.entities.Course;
import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecialization;
import com.lonn.studentassistant.viewModels.entities.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryGenerator {
    public static <T extends Course> List<CategoryViewModel<T>> semesterOptionalityCategories(CategoryViewModel<T> parentCategory) {
        List<CategoryViewModel<T>> subcategories = new ArrayList<>();

        subcategories.add(new CategoryViewModel<T>()
                .setCategoryTitle("First Semester")
                .setEntityName(parentCategory.getEntityName())
                .setViewType(parentCategory.getViewType())
                .setPermissionLevel(parentCategory.getPermissionLevel())
                .setShowEmpty(parentCategory.isShowEmpty())
                .setShowHeader(true)
                .setShouldContain((discipline) -> discipline.getSemester() == 1));

        subcategories.add(new CategoryViewModel<T>()
                .setCategoryTitle("Second Semester")
                .setEntityName(parentCategory.getEntityName())
                .setViewType(parentCategory.getViewType())
                .setPermissionLevel(parentCategory.getPermissionLevel())
                .setShowEmpty(parentCategory.isShowEmpty())
                .setShowHeader(true)
                .setShouldContain((discipline) -> discipline.getSemester() == 2));

        return subcategories;
    }

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
                    .setChildCategories(yearSemesterOptionalityCategories(parentCategory)));
        }

        return subcategories;
    }

    public static <T extends Course> List<CategoryViewModel<T>> yearSemesterOptionalityCategories(CategoryViewModel<T> parentCategory) {
        List<CategoryViewModel<T>> subcategories = new ArrayList<>();

        subcategories.add(new CategoryViewModel<T>()
                .setCategoryTitle("First year")
                .setEntityName(parentCategory.getEntityName())
                .setViewType(parentCategory.getViewType())
                .setPermissionLevel(parentCategory.getPermissionLevel())
                .setShowEmpty(parentCategory.isShowEmpty())
                .setShowHeader(true)
                .setShouldContain((discipline) -> discipline.getYear() == 1)
                .setChildCategories(semesterOptionalityCategories(parentCategory)));

        subcategories.add(new CategoryViewModel<T>()
                .setCategoryTitle("Second year")
                .setEntityName(parentCategory.getEntityName())
                .setViewType(parentCategory.getViewType())
                .setPermissionLevel(parentCategory.getPermissionLevel())
                .setShowEmpty(parentCategory.isShowEmpty())
                .setShowHeader(true)
                .setShouldContain((discipline) -> discipline.getYear() == 2)
                .setChildCategories(semesterOptionalityCategories(parentCategory)));

        subcategories.add(new CategoryViewModel<T>()
                .setCategoryTitle("Third year")
                .setEntityName(parentCategory.getEntityName())
                .setViewType(parentCategory.getViewType())
                .setPermissionLevel(parentCategory.getPermissionLevel())
                .setShowEmpty(parentCategory.isShowEmpty())
                .setShowHeader(true)
                .setShouldContain((discipline) -> discipline.getYear() == 3)
                .setChildCategories(semesterOptionalityCategories(parentCategory)));

        return subcategories;
    }
}
