package com.lonn.studentassistant.services.loginService;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.authentication.AuthSharedPrefs;
import com.lonn.studentassistant.common.ActivityServiceConnections;
import com.lonn.studentassistant.common.responses.LoginResponse;
import com.lonn.studentassistant.common.responses.Response;
import com.lonn.studentassistant.common.interfaces.IServiceCallback;
import com.lonn.studentassistant.common.requests.DatabaseRequest;
import com.lonn.studentassistant.common.requests.LoginRequest;
import com.lonn.studentassistant.common.requests.Request;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.common.abstractClasses.BasicService;
import com.lonn.studentassistant.entities.User;
import com.lonn.studentassistant.services.userService.UserService;

public class LoginService extends BasicService implements IServiceCallback
{
    protected ActivityServiceConnections serviceConnections;
    private AuthSharedPrefs authSharedPrefs;
    private LoginRequest request;

    @Override
    public void onCreate()
    {
        super.onCreate();

        authSharedPrefs = new AuthSharedPrefs();
        serviceConnections = new ActivityServiceConnections(UserService.class);
        serviceConnections.bind(this);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        serviceConnections.unbind(this);
    }

    public void postRequest(Request incomingRequest)
    {
        if (request != null)
        {
            sendResponse(new Response<User>(User.class, "login", "A login already is in progress!", null));
        }
        else if (incomingRequest.action.equals("login") && incomingRequest instanceof LoginRequest)
        {
            request = (LoginRequest)incomingRequest;
            serviceConnections.getServiceByClass(UserService.class).postRequest(new DatabaseRequest("getById", Utils.emailToKey(request.email)));
        }
    }

    public void processResponse(Response response)
    {
        if (response.action.equals("getById") && response.type.equals(User.class))
        {
            if (response.result.equals("success"))
            {
                if (response.items.size() == 1)
                {
                    signIn((User)response.items.get(0));
                }
                else
                {
                    sendResponse(new Response<User>(User.class, "login", "fail", null));
                    request = null;
                }
            }
            else
            {
                sendResponse(new Response<User>(User.class, "login", "fail", null));
                request = null;
            }
        }
    }

    private void signIn(final User user)
    {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(request.email, request.password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            if (request.remember)
                            {
                                authSharedPrefs.rememberCredentials(request.email, request.password);
                            }

                            sendResponse(new LoginResponse(request.email, request.password, request.remember, user.getPrivileges()));
                        }
                        else
                        {
                            authSharedPrefs.deleteCredentials();

                            sendResponse(new LoginResponse(getResources().getString(R.string.invalid_credentials)));
                        }
                    }
                });
    }
}
