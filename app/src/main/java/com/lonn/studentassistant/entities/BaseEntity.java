package com.lonn.studentassistant.entities;

import java.io.Serializable;

public abstract class BaseEntity implements Serializable
{
    public abstract String getKey();
    public abstract void setKey(String key);
}
