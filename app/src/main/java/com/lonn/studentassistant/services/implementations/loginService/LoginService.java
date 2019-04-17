package com.lonn.studentassistant.services.implementations.loginService;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.IDatabaseCallback;
import com.lonn.studentassistant.activities.implementations.authentication.AuthSharedPrefs;
import com.lonn.studentassistant.common.ActivityServiceConnections;
import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.common.requests.GetByIdRequest;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.common.responses.LoginResponse;
import com.lonn.studentassistant.common.requests.LoginRequest;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.services.abstractions.BasicService;
import com.lonn.studentassistant.entities.User;
import com.lonn.studentassistant.services.implementations.userService.UserService;

public class LoginService extends BasicService<LoginResponse>
{
    protected ActivityServiceConnections serviceConnections = new ActivityServiceConnections(UserService.class);
    private AuthSharedPrefs authSharedPrefs;
    private LoginRequest request;

    @Override
    public void onCreate()
    {
        super.onCreate();
        authSharedPrefs = new AuthSharedPrefs();
        serviceConnections.bind(userCallback, this);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        serviceConnections.unbind(userCallback, this);
    }

    public void postRequest(LoginRequest incomingRequest)
    {
        if (request != null)
        {
            sendResponse(new LoginResponse("login", "A login already is in progress!",false, null));
        }
        else
        {
            request = incomingRequest;
            ((UserService)serviceConnections.getServiceByClass(UserService.class)).postRequest(new GetByIdRequest<User>(Utils.emailToKey(request.email)));
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

    private IDatabaseCallback<User> userCallback = new IDatabaseCallback<User>()
    {
        public void processResponse(DatabaseResponse<User> response) {}
        public void processResponse(CreateResponse<User> response){}
        public void processResponse(EditResponse<User> response){}
        public void processResponse(DeleteResponse<User> response){}
        public void processResponse(GetAllResponse<User> response){}

        public void processResponse(GetByIdResponse<User> response)
        {
                if (response.getResult().equals("success"))
                {
                    if (response.getItems().size() == 1)
                    {
                        signIn(response.getItems().get(0));
                    }
                    else
                    {
                        sendResponse(new LoginResponse("Invalid credentials"));
                        request = null;
                    }
                }
                else
                {
                    sendResponse(new LoginResponse("Invalid credentials"));
                    request = null;
                }
        }
    };
}
