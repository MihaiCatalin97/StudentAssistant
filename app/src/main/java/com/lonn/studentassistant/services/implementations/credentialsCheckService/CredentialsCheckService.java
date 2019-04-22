package com.lonn.studentassistant.services.implementations.credentialsCheckService;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.IDatabaseCallback;
import com.lonn.studentassistant.activities.implementations.authentication.AuthSharedPrefs;
import com.lonn.studentassistant.common.ActivityServiceConnections;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.common.requests.CredentialsRequest;
import com.lonn.studentassistant.common.requests.GetByIdRequest;
import com.lonn.studentassistant.common.requests.LoginRequest;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.CredentialsResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.common.responses.LoginResponse;
import com.lonn.studentassistant.entities.BaseEntity;
import com.lonn.studentassistant.entities.Student;
import com.lonn.studentassistant.entities.User;
import com.lonn.studentassistant.services.abstractions.BasicService;
import com.lonn.studentassistant.services.implementations.studentService.StudentService;
import com.lonn.studentassistant.services.implementations.userService.UserService;

public class CredentialsCheckService extends BasicService<CredentialsResponse>
{
    protected ActivityServiceConnections serviceConnections = new ActivityServiceConnections(StudentService.class);
    private CredentialsRequest request;

    @Override
    public void onCreate()
    {
        super.onCreate();
        serviceConnections.bind(StudentService.class, studentCallback, this);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        serviceConnections.unbind(studentCallback, this);
    }

    public void postRequest(CredentialsRequest incomingRequest)
    {
        if (request != null)
        {
            sendResponse(new CredentialsResponse<>("A credential check is in progress!", (Student) null));
        }
        else
        {
            request = incomingRequest;
            ((StudentService)serviceConnections.getServiceByClass(StudentService.class)).postRequest(new GetByIdRequest<Student>(request.getEntity().getKey()));
        }
    }

    private IDatabaseCallback<Student> studentCallback = new IDatabaseCallback<Student>()
    {
        public void processResponse(DatabaseResponse<Student> response) {}
        public void processResponse(CreateResponse<Student> response){}
        public void processResponse(EditResponse<Student> response){}
        public void processResponse(DeleteResponse<Student> response){}
        public void processResponse(GetAllResponse<Student> response){}

        public void processResponse(GetByIdResponse<Student> response)
        {
            if (response.getResult().equals("success"))
            {
                if (response.getItems().size() == 1)
                {
                    sendResponse(new CredentialsResponse<>("success", response.getItems().get(0)));
                }
                else
                {
                    sendResponse(new CredentialsResponse<>("Invalid credentials", (Student)null));
                    request = null;
                }
            }
            else
            {
                sendResponse(new CredentialsResponse<>("Invalid credentials", response.getItems()));
                request = null;
            }
        }
    };
}
