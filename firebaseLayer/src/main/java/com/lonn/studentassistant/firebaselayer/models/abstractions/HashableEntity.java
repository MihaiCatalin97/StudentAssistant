package com.lonn.studentassistant.firebaselayer.models.abstractions;

public abstract class HashableEntity extends BaseEntity {
    public abstract String computeHashingString();
}
