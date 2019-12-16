package com.lonn.studentassistant.views;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.Discipline;
import com.lonn.studentassistant.viewModels.entities.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryGenerator {
    public static <T extends Discipline> List<CategoryViewModel<T>> semesterCategories(CategoryViewModel<T> parentCategory) {
        List<CategoryViewModel<T>> subcategories = new ArrayList<>();

        subcategories.add(new CategoryViewModel<T>()
                .setCategoryTitle("First Semester")
                .setEntityName(parentCategory.getEntityName())
                .setViewType(parentCategory.getViewType())
                .setPermissionLevel(parentCategory.getPermissionLevel())
                .setShowEmpty(parentCategory.isShowEmpty())
                .setShowHeader(parentCategory.isShowHeader())
                .setShouldContain((discipline) -> discipline.getSemester() == 1));

        subcategories.add(new CategoryViewModel<T>()
                .setCategoryTitle("Second Semester")
                .setEntityName(parentCategory.getEntityName())
                .setViewType(parentCategory.getViewType())
                .setPermissionLevel(parentCategory.getPermissionLevel())
                .setShowEmpty(parentCategory.isShowEmpty())
                .setShowHeader(parentCategory.isShowHeader())
                .setShouldContain((discipline) -> discipline.getSemester() == 2));

        return subcategories;
    }
}
