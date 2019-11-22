package com.lonn.studentassistant.firebaselayer.predicates.fields;

import com.lonn.studentassistant.firebaselayer.models.abstractions.BaseEntity;

public abstract class EntityFields<T extends BaseEntity, X> {
    public abstract String getFieldName();
}
