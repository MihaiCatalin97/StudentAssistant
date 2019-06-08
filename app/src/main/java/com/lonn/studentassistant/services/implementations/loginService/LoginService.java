package com.lonn.studentassistant.services.implementations.loginService;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.ICallback;
import com.lonn.studentassistant.activities.abstractions.IDatabaseCallback;
import com.lonn.studentassistant.activities.implementations.authentication.AuthSharedPrefs;
import com.lonn.studentassistant.common.ConnectionBundle;
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
    protected ConnectionBundle serviceConnections;
    private AuthSharedPrefs authSharedPrefs;
    private LoginRequest request;
    private ICallback<LoginResponse> callback;

    @Override
    public void onCreate()
    {
        super.onCreate();
        authSharedPrefs = new AuthSharedPrefs();

        serviceConnections = new ConnectionBundle(getBaseContext());
    }

    @Override
    public void onDestroy()
    {
        serviceConnections.unbind(userCallback);
        super.onDestroy();
    }

    public void postRequest(LoginRequest incomingRequest, ICallback<LoginResponse> callback)
    {
        if (request != null)
        {
            sendResponse(new LoginResponse("A login already is in progress!"), callback);
        }
        else
        {
            request = incomingRequest;
            this.callback = callback;

            serviceConnections.postRequest(UserService.class, new GetByIdRequest<User>(Utils.emailToKey(request.email)), userCallback);
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

                            sendResponse(new LoginResponse(user, request.password, request.remember), callback);
                            request = null;
                        }
                        else
                        {
                            authSharedPrefs.deleteCredentials();

                            sendResponse(new LoginResponse(getResources().getString(R.string.invalid_credentials)), callback);
                            request = null;
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
                        sendResponse(new LoginResponse("Invalid credentials"), callback);
                        request = null;
                    }
                }
                else
                {
                    sendResponse(new LoginResponse("Invalid credentials"), callback);
                    request = null;
                }
        }
    };
}
