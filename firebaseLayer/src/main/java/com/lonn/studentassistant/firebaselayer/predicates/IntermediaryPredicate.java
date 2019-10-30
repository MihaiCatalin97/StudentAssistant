package com.lonn.studentassistant.firebaselayer.predicates;

import com.lonn.studentassistant.firebaselayer.models.BaseEntity;
import com.lonn.studentassistant.firebaselayer.predicates.fields.EntityFields;
import com.lonn.studentassistant.firebaselayer.predicates.operators.Equal;
import com.lonn.studentassistant.firebaselayer.predicates.operators.GreaterEqual;
import com.lonn.studentassistant.firebaselayer.predicates.operators.LessEqual;

import lombok.AccessLevel;
import lombok.Getter;

public class IntermediaryPredicate<T extends BaseEntity, V> {
    @Getter(AccessLevel.PROTECTED)
    private EntityFields<T, V> field;

    IntermediaryPredicate(EntityFields<T, V> field) {
        this.field = field;
    }

    public Predicate<T> equalTo(V value) {
        return new Predicate<>(field, new Equal<>().apply(value));
    }

    public Predicate<T> lessEqual(V value) {
        return new Predicate<>(field, new LessEqual<>().apply(value));
    }

    public Predicate<T> greaterEqual(V value) {
        return new Predicate<>(field, new GreaterEqual<>().apply(value));
    }
}