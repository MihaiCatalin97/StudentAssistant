package com.lonn.studentassistant.activities.abstractions;

import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.entities.BaseEntity;

public interface IGetAllCallback<T extends BaseEntity> extends ICallback<GetAllResponse<T>>
{
    void processResponse(GetAllResponse<T> response);
}
