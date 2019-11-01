package com.lonn.studentassistant.common;

import com.lonn.studentassistant.firebaselayer.models.BaseEntity;

@FunctionalInterface
public interface Predicate<T extends BaseEntity> {
    boolean test(T entity);
}
