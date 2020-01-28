package com.lonn.studentassistant.validation.predicates;

import com.lonn.studentassistant.firebaselayer.entities.enums.CycleSpecializationYear;
import com.lonn.studentassistant.functionalIntefaces.Function;
import com.lonn.studentassistant.functionalIntefaces.Predicate;

import java.util.Arrays;

public class MiscValidationPredicates {
	public static <T> Predicate<T> isValidCycleSpecialization(Function<T, CycleSpecializationYear> getter) {
		return entity -> getter.apply(entity) != null;
	}

	public static <T> Predicate<T> isValidGroupForCycleSpecializationYear(Function<T, CycleSpecializationYear> cycleSpecializationGetter,
																		  Function<T, String> groupGetter) {
		return entity -> Arrays.asList(cycleSpecializationGetter.apply(entity).getGroups()).contains(groupGetter.apply(entity));
	}
}
