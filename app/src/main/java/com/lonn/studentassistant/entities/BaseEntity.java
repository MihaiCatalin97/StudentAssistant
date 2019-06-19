package com.lonn.studentassistant.entities;

import java.io.Serializable;

public abstract class BaseEntity implements Serializable, Comparable
{
    public abstract String getKey();
    public abstract void setKey(String key);
}
