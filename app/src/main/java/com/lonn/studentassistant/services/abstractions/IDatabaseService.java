package com.lonn.studentassistant.services.abstractions;

import com.lonn.studentassistant.activities.abstractions.callbacks.ICallback;
import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.common.requests.CreateRequest;
import com.lonn.studentassistant.common.requests.DeleteRequest;
import com.lonn.studentassistant.common.requests.GetAllRequest;
import com.lonn.studentassistant.common.requests.GetByIdRequest;
import com.lonn.studentassistant.common.requests.EditRequest;
import com.lonn.studentassistant.entities.BaseEntity;

public interface IDatabaseService<T extends BaseEntity>
{
    void postRequest(CreateRequest<T> request, ICallback<DatabaseResponse<T>> callback);
    void postRequest(DeleteRequest<T> request, ICallback<DatabaseResponse<T>> callback);
    void postRequest(GetAllRequest<T> request, ICallback<DatabaseResponse<T>> callback);
    void postRequest(GetByIdRequest<T> request, ICallback<DatabaseResponse<T>> callback);
    void postRequest(EditRequest<T> request, ICallback<DatabaseResponse<T>> callback);
}
