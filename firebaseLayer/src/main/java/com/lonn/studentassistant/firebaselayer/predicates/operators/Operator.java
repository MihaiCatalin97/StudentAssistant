package com.lonn.studentassistant.firebaselayer.predicates.operators;

import com.google.firebase.database.Query;

public abstract class Operator<V> {
    public abstract OperatorFilter apply(V value);

    @FunctionalInterface
    public interface OperatorFilter {
        Query apply(Query query);
    }
}
