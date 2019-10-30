package com.lonn.studentassistant.firebaselayer.models;

import java.io.Serializable;

public abstract class BaseEntity implements Serializable {
    public abstract String computeKey();
}
