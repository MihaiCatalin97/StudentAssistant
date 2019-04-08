package com.lonn.studentassistant.authentication.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.lonn.studentassistant.entities.Student;
import com.lonn.studentassistant.globalServices.studentService.StudentService;

public class CredentialsCheckService extends IntentService
{
    public CredentialsCheckService()
    {
        super("CredentialsCheck Service");
    }

    @Override
    public void onHandleIntent(Intent intent)
    {
        final Student inputStudent = (Student)intent.getSerializableExtra("inputStudent");

        if (intent.getAction() != null)
        {
            Intent getByIdIntent = new Intent(getBaseContext(), StudentService.class);

            getByIdIntent.putExtra("action","getById");
            getByIdIntent.putExtra("id", inputStudent.numarMatricol);
            getByIdIntent.putExtra("callbackClass", this.getClass().getName());
            getByIdIntent.putExtras(intent);

            startService(getByIdIntent);
        }
        else
        {
            Intent intent1 = new Intent("credentials");

            final Student databaseStudent = (Student)intent.getSerializableExtra("getById-result");

            if (databaseStudent != null)
            {
                if (databaseStudent.accountId == null)
                {
                    databaseStudent.numarMatricol = inputStudent.numarMatricol;

                    if (inputStudent.equals(databaseStudent))
                        intent1.putExtra("result","success");
                    else
                        intent1.putExtra("result","Invalid credentials!");
                }
                else
                    intent1.putExtra("result","Student already has an account!");
            }
            else
                intent1.putExtra("result","Invalid credentials!");

            sendBroadcast(intent1);
        }
    }
}
