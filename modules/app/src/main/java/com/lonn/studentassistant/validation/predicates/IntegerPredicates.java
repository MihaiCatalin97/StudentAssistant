package com.lonn.studentassistant.validation.predicates;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.validation.ValidationConstants;
import com.lonn.studentassistant.functionalIntefaces.IntegerGetter;
import com.lonn.studentassistant.functionalIntefaces.Predicate;

public class IntegerPredicates {
    public static <T extends BaseEntity> Predicate<T> isValidYear(IntegerGetter<T> getter) {
        return (student) -> {
            Integer fieldValue = getter.getIntegerField(student);
            return fieldValue != null && fieldValue >= ValidationConstants.MIN_YEAR &&
                    fieldValue <= ValidationConstants.MAX_YEAR;
        };
    }

    public static <T extends BaseEntity> Predicate<T> isValidSemester(IntegerGetter<T> getter) {
        return (student) -> {
            Integer fieldValue = getter.getIntegerField(student);
            return fieldValue != null && fieldValue >= ValidationConstants.MIN_SEMESTER &&
                    fieldValue < +ValidationConstants.MAX_SEMESTER;
        };
    }
}
