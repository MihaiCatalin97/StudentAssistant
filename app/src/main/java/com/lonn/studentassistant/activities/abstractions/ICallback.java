package com.lonn.studentassistant.activities.abstractions;

import com.lonn.studentassistant.common.abstractions.Response;

public interface ICallback<T extends Response>
{
    void processResponse(T response);
}
