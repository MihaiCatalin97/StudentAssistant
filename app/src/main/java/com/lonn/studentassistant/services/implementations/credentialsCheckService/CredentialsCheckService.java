package com.lonn.studentassistant.services.implementations.credentialsCheckService;

import com.lonn.studentassistant.activities.abstractions.ICallback;
import com.lonn.studentassistant.activities.abstractions.IDatabaseCallback;
import com.lonn.studentassistant.common.ConnectionBundle;
import com.lonn.studentassistant.common.abstractions.DatabaseResponse;
import com.lonn.studentassistant.common.requests.CredentialsRequest;
import com.lonn.studentassistant.common.requests.GetByIdRequest;
import com.lonn.studentassistant.common.responses.CreateResponse;
import com.lonn.studentassistant.common.responses.CredentialsResponse;
import com.lonn.studentassistant.common.responses.DeleteResponse;
import com.lonn.studentassistant.common.responses.EditResponse;
import com.lonn.studentassistant.common.responses.GetAllResponse;
import com.lonn.studentassistant.common.responses.GetByIdResponse;
import com.lonn.studentassistant.entities.Student;
import com.lonn.studentassistant.services.abstractions.BasicService;
import com.lonn.studentassistant.services.implementations.studentService.StudentService;

public class CredentialsCheckService extends BasicService<CredentialsResponse>
{
    protected ConnectionBundle serviceConnections;
    private CredentialsRequest request;
    private ICallback<CredentialsResponse> callback;

    @Override
    public void onCreate()
    {
        super.onCreate();
        serviceConnections = new ConnectionBundle(getBaseContext());
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        serviceConnections.unbind(studentCallback);
    }

    public void postRequest(CredentialsRequest incomingRequest, ICallback<CredentialsResponse> callback)
    {
        if (request != null)
        {
            sendResponse(new CredentialsResponse<>("A credential check is in progress!", (Student) null), callback);
        }
        else
        {
            request = incomingRequest;
            this.callback = callback;
            serviceConnections.postRequest(StudentService.class, new GetByIdRequest<Student>(request.getEntity().getKey()), studentCallback);
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
                    sendResponse(new CredentialsResponse<>("success", response.getItems().get(0)), callback);
                }
                else
                {
                    sendResponse(new CredentialsResponse<>("Invalid credentials", (Student)null), callback);
                    request = null;
                }
            }
            else
            {
                sendResponse(new CredentialsResponse<>("Invalid credentials", response.getItems()), callback);
                request = null;
            }
        }
    };
}
