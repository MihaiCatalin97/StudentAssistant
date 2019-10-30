package com.lonn.studentassistant.firebaselayer.predicates;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.lonn.studentassistant.firebaselayer.models.BaseEntity;
import com.lonn.studentassistant.firebaselayer.predicates.fields.EntityFields;
import com.lonn.studentassistant.firebaselayer.predicates.operators.Operator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Predicate<T extends BaseEntity> {
    @Getter(AccessLevel.PROTECTED)
    private EntityFields<T, ?> field;
    @Getter(AccessLevel.PROTECTED)
    private Operator.OperatorFilter operatorFilter;

    public static <T extends BaseEntity, V> IntermediaryPredicate<T, V> where(EntityFields<T, V> field) {
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
