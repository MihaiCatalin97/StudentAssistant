package com.lonn.studentassistant.activities.abstractions;

import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.common.abstractions.Response;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.BaseEntity;

import java.util.Collections;

public abstract class AbstractDatabaseCallback<T extends BaseEntity> implements IDatabaseCallback<T>
{
    protected ServiceBoundActivity activity;
    protected IBusinessLayer businessLayer;

    public AbstractDatabaseCallback(ServiceBoundActivity activity)
    {
        this.activity = activity;
    }

    public AbstractDatabaseCallback(ServiceBoundActivity activity, IBusinessLayer businessLayer)
    {
        this.activity = activity;
        this.businessLayer = businessLayer;
    }

    public void processResponse(DatabaseResponse<T> response)
    {
        activity.hideSnackbar();
        Collections.sort(response.getItems());

        if(!response.getResult().equals("success"))
            activity.showSnackbar("Failed " + response.getAction());
        else if (response.getAction().equals(getExpectedAction(response)))
        {
            if (response instanceof CreateResponse)
                this.processResponse((CreateResponse<T>) response);
            else if (response instanceof DeleteResponse)
                this.processResponse((DeleteResponse<T>) response);
            else if (response instanceof EditResponse)
                this.processResponse((EditResponse<T>) response);
            else if (response instanceof GetAllResponse)
                this.processResponse((GetAllResponse<T>) response);
            else if (response instanceof GetByIdResponse)
                this.processResponse((GetByIdResponse<T>) response);
        }
    }

    private String getExpectedAction(Response response)
    {
        String className = response.getClass().getSimpleName();
        return className.substring(0,1).toLowerCase() + className.substring(1, className.length()-9);
    }
}
