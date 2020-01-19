package com.lonn.studentassistant.firebaselayer.entities.abstractions;

public interface HashableEntity {
    String computeHashingString();

    String getKey();
}
