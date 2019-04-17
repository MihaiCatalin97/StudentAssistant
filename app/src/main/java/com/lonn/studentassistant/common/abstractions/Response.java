package com.lonn.studentassistant.common.abstractions;

public abstract class Response
{
    private String action;
    private String result;

    protected Response(String action, String result)
    {
        this.action = action;
        this.result = result;
    }

    public String getAction()
    {
        return action;
    }

    public String getResult()
    {
        return result;
    }
}
