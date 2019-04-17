package com.lonn.studentassistant.common.abstractions;

public abstract class Request
{
    public String action;

    public Request(String action)
    {
        this.action = action;
    }
}
