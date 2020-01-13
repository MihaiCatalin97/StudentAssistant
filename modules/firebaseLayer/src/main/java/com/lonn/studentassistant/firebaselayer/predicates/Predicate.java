package com.lonn.studentassistant.firebaselayer.predicates;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.predicates.fields.EntityField;
import com.lonn.studentassistant.firebaselayer.predicates.operators.Operator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Predicate<T extends BaseEntity, V> {
	@Getter(AccessLevel.PUBLIC)
	private EntityField<T, V> field;
	@Getter(AccessLevel.PUBLIC)
	private Operator.OperatorFilter operatorFilter;
	@Getter(AccessLevel.PUBLIC)
	private Class<? extends Operator> operatorClass;
	@Getter(AccessLevel.PUBLIC)
	private V value;

	public static <T extends BaseEntity, V> IntermediaryPredicate<T, V> where(EntityField<T, V> field) {
		return new IntermediaryPredicate<>(field);
	}

	@NonNull
	public Query apply(DatabaseReference database) {
		Query query;

		if (field.getFieldName().toLowerCase().equals("id")) {
			query = database.orderByKey();
		}
		else {
			query = database.orderByChild(field.getFieldName());
		}

		return operatorFilter.apply(query);
	}
}
