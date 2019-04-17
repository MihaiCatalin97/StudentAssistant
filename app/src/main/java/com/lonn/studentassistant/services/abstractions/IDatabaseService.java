package com.lonn.studentassistant.services.abstractions;

import com.lonn.studentassistant.activities.abstractions.IServiceCallback;
import com.lonn.studentassistant.common.requests.CreateRequest;
import com.lonn.studentassistant.common.requests.DeleteRequest;
import com.lonn.studentassistant.common.requests.GetAllRequest;
import com.lonn.studentassistant.common.requests.GetByIdRequest;
import com.lonn.studentassistant.common.requests.UpdateRequest;
import com.lonn.studentassistant.entities.BaseEntity;

public interface IService<T extends BaseEntity>
{
    void addCallback(IServiceCallback callback);
    void removeCallback(IServiceCallback callback);
    void postRequest(CreateRequest<T> request);
    void postRequest(DeleteRequest<T> request);
    void postRequest(GetAllRequest<T> request);
    void postRequest(GetByIdRequest<T> request);
    void postRequest(UpdateRequest<T> request);
}
