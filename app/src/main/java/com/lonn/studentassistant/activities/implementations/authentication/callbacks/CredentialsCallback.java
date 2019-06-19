package com.lonn.studentassistant.activities.implementations.authentication.callbacks;

import com.lonn.studentassistant.activities.abstractions.ICallback;
import com.lonn.studentassistant.activities.implementations.authentication.AuthenticationActivity;
import com.lonn.studentassistant.common.responses.CredentialsResponse;

public class CredentialsCallback implements ICallback<CredentialsResponse>
{
    private AuthenticationActivity activity;

    public CredentialsCallback(AuthenticationActivity activity)
    {
        this.activity = activity;
    }

    public void processResponse(final CredentialsResponse response)
    {
        activity.hideSnackbar();

        if (response.getResult().equals("success"))
        {
            activity.showRegistrationStep(2);
        }
        else
        {
            activity.showSnackbar(response.getResult());
        }
    }
}
