package com.lonn.studentassistant.activities.abstractions;

import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.BaseEntity;

public interface IDatabaseCallback<T extends BaseEntity> extends ICallback<DatabaseResponse<T>>
{
    void processResponse(CreateResponse<T> response);
    void processResponse(DeleteResponse<T> response);
    void processResponse(EditResponse<T> response);
    void processResponse(GetAllResponse<T> response);
    void processResponse(GetByIdResponse<T> response);
}
