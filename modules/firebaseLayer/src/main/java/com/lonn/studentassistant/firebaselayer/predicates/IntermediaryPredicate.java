package com.lonn.studentassistant.firebaselayer.predicates;

import com.lonn.studentassistant.firebaselayer.entities.abstractions.BaseEntity;
import com.lonn.studentassistant.firebaselayer.predicates.fields.EntityField;
import com.lonn.studentassistant.firebaselayer.predicates.operators.Equal;
import com.lonn.studentassistant.firebaselayer.predicates.operators.GreaterEqual;
import com.lonn.studentassistant.firebaselayer.predicates.operators.LessEqual;

import lombok.AccessLevel;
import lombok.Getter;

public class IntermediaryPredicate<T extends BaseEntity, V> {
	@Getter(AccessLevel.PROTECTED)
	private EntityField<T, V> field;

	IntermediaryPredicate(EntityField<T, V> field) {
		this.field = field;
	}

	public Predicate<T, V> equalTo(V value) {
		return new Predicate<>(field, new Equal<>().apply(value), Equal.class, value);
	}

	public Predicate<T, V> lessEqual(V value) {
		return new Predicate<>(field, new LessEqual<>().apply(value), LessEqual.class, value);
	}

	public Predicate<T, V> greaterEqual(V value) {
		return new Predicate<>(field, new GreaterEqual<>().apply(value), GreaterEqual.class, value);
	}
}