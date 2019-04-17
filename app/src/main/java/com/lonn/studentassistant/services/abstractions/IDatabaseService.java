package com.lonn.studentassistant.services.abstractions;

import com.lonn.studentassistant.common.requests.CreateRequest;
import com.lonn.studentassistant.common.requests.DeleteRequest;
import com.lonn.studentassistant.common.requests.GetAllRequest;
import com.lonn.studentassistant.common.requests.GetByIdRequest;
import com.lonn.studentassistant.common.requests.EditRequest;
import com.lonn.studentassistant.entities.BaseEntity;

public interface IDatabaseService<T extends BaseEntity>
{
    void postRequest(CreateRequest<T> request);
    void postRequest(DeleteRequest<T> request);
    void postRequest(GetAllRequest<T> request);
    void postRequest(GetByIdRequest<T> request);
    void postRequest(EditRequest<T> request);
}
