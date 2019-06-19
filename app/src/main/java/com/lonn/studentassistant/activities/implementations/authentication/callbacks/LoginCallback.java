package com.lonn.studentassistant.activities.implementations.authentication.callbacks;

import android.content.Intent;

import com.lonn.studentassistant.activities.abstractions.ICallback;
import com.lonn.studentassistant.activities.implementations.authentication.AuthenticationActivity;
import com.lonn.studentassistant.activities.implementations.student.StudentActivity;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.common.responses.LoginResponse;

import java.util.HashMap;

public class LoginCallback implements ICallback<LoginResponse>
{
    AuthenticationActivity activity;

    public LoginCallback(AuthenticationActivity activity)
    {
        this.activity = activity;
    }

    public void processResponse(final  LoginResponse response)
    {
        activity.hideSnackbar();

        if(response.getResult().equals("success"))
        {
            if (response.remember)
            {
                activity.setLoginFields(new HashMap<String,String>()
                {{
                    put("remember", "true");
                    put("email", Utils.keyToEmail(response.user.getKey()));
                    put("password", response.password);
                }});
            }

            if (response.user.getPrivileges().equals("student"))
            {
                activity.getStudent(response.user.getPersonId());
            }
        }
        else
        {
            activity.showSnackbar(response.getResult());
        }
    }
}
