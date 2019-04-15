package com.lonn.studentassistant.common.interfaces;

import com.lonn.studentassistant.common.requests.Request;

public interface IService
{
    void addCallback(IServiceCallback callback);
    void removeCallback(IServiceCallback callback);
    void postRequest(Request request);
}
