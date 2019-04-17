package com.lonn.studentassistant.activities.implementations.authentication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.ICallback;
import com.lonn.studentassistant.activities.implementations.debug.DebugActivity;
import com.lonn.studentassistant.common.responses.LoginResponse;
import com.lonn.studentassistant.common.requests.LoginRequest;
import com.lonn.studentassistant.activities.abstractions.ServiceBoundActivity;
import com.lonn.studentassistant.services.implementations.credentialsCheckService.CredentialsCheckService;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.entities.Student;
import com.lonn.studentassistant.services.implementations.loginService.LoginService;
import com.lonn.studentassistant.services.implementations.registerService.RegisterService;
import com.lonn.studentassistant.activities.implementations.student.StudentActivity;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationActivity extends ServiceBoundActivity
{
    private AuthSharedPrefs authSharedPrefs;
    private RegisterReceiver registerReceiver;
    private Student registeringStudent;
    private CredentialsCheckReceiver credentialsReceiver;
    private String privileges;

    public AuthenticationActivity()
    {
        super(LoginService.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity_layout);
        AuthSharedPrefs.init(getBaseContext());

        authSharedPrefs = new AuthSharedPrefs();

        if (authSharedPrefs.hasSavedCredentials())
            setLoginFields(authSharedPrefs.getCredentials());
    }

    @Override
    public void onStart() {
        super.onStart();
        serviceConnections.bind(loginCallback, this);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (registerReceiver == null)
        {
            registerReceiver = new RegisterReceiver();
            registerReceiver(registerReceiver, new IntentFilter("register"));
        }

        if (credentialsReceiver == null)
        {
            credentialsReceiver = new CredentialsCheckReceiver();
            registerReceiver(credentialsReceiver, new IntentFilter("credentials"));
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (registerReceiver != null)
            unregisterReceiver(registerReceiver);
        if (credentialsReceiver != null)
            unregisterReceiver(credentialsReceiver);

        registerReceiver = null;
        credentialsReceiver = null;
    }

    @Override
    public void onStop()
    {
        super.onStop();
        serviceConnections.unbind(loginCallback, this);
    }

    public void login(View v)
    {
        String email = ((EditText)findViewById(R.id.loginEditTextEmail)).getText().toString();
        String password = ((EditText)findViewById(R.id.loginEditTextPassword)).getText().toString();
        boolean remember = ((CheckBox)findViewById(R.id.loginRememberCheckBox)).isChecked();

        if (email.length() == 0)
        {
            Toast.makeText(getBaseContext(),"Invalid email!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() == 0)
        {
            Toast.makeText(getBaseContext(),"Invalid password!",Toast.LENGTH_SHORT).show();
            return;
        }

        ((LoginService)serviceConnections.getServiceByClass(LoginService.class)).postRequest(new LoginRequest(email, password, remember));
    }

    public void showRegistrationStep(int step)
    {
        switch(step)
        {
            case 0:
            {
                findViewById(R.id.layoutLogin).setVisibility(View.INVISIBLE);
                findViewById(R.id.layoutRegister1).setVisibility(View.VISIBLE);
                break;
            }
            case 1:
            {
                findViewById(R.id.layoutRegister1).setVisibility(View.INVISIBLE);
                findViewById(R.id.layoutRegister2).setVisibility(View.VISIBLE);
                break;
            }
            case 2:
            {
                findViewById(R.id.layoutRegister2).setVisibility(View.INVISIBLE);
                findViewById(R.id.layoutRegister3).setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    private void checkCredentials()
    {
        String numarMatricol = ((EditText)findViewById(R.id.registerNumarMatricolEditText)).getText().toString();
        String firstName = ((EditText)findViewById(R.id.registerFirstNameEditText)).getText().toString();
        String lastName = ((EditText)findViewById(R.id.registerLastNameEditText)).getText().toString();
        String initialaTata = ((EditText)findViewById(R.id.registerFatherEditText)).getText().toString();
        String email = ((EditText)findViewById(R.id.registerEmailEditText)).getText().toString();
        String phoneNumber = ((EditText)findViewById(R.id.registerPhoneEditText)).getText().toString();
        int an;
        String grupa = ((EditText)findViewById(R.id.registerGroupEditText)).getText().toString();

        if (!Utils.isValidStudentId(numarMatricol)) {
            Toast.makeText(getBaseContext(), "Invalid student ID!", Toast.LENGTH_SHORT).show();
            return;
        }

        try
        {
            an = Integer.parseInt(((EditText)findViewById(R.id.registerYearEditText)).getText().toString());
        }
        catch (Exception e)
        {
            Toast.makeText(getBaseContext(), "Invalid year!", Toast.LENGTH_SHORT).show();
            return;
        }

        registeringStudent = new Student(numarMatricol,lastName,firstName,initialaTata,email,phoneNumber,an,grupa);

        Intent credentialsIntent = new Intent(this, CredentialsCheckService.class);
        credentialsIntent.setAction("check");
        credentialsIntent.putExtra("inputStudent", registeringStudent);
        startService(credentialsIntent);
    }

    private void register()
    {
        String email = ((EditText)findViewById(R.id.registerEditTextEmail)).getText().toString();
        String password = ((EditText)findViewById(R.id.registerEditTextPassword)).getText().toString();
        String password2 = ((EditText)findViewById(R.id.registerEditTextPassword2)).getText().toString();

        if (email.length() == 0)
        {
            Toast.makeText(getBaseContext(),getResources().getString(R.string.invalid_email),Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() == 0)
        {
            Toast.makeText(getBaseContext(),getResources().getString(R.string.invalid_password),Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(password2))
        {
            Toast.makeText(getBaseContext(),getResources().getString(R.string.invalid_password_match),Toast.LENGTH_SHORT).show();
            return;
        }

        registeringStudent.accountId = email;

        Intent registerIntent = new Intent(this, RegisterService.class);
        registerIntent.putExtra("email", email);
        registerIntent.putExtra("password", password);
        registerIntent.putExtra("privileges", privileges);
        registerIntent.putExtra("registeringStudent", registeringStudent);
        startService(registerIntent);
    }

    public void showDebugLayout(View v)
    {
        Intent debugIntent = new Intent(this, DebugActivity.class);
        startActivity(debugIntent);
    }



    public void tapRegistrationButton(View v)
    {
        switch (v.getId())
        {
            case R.id.signUpButton:
            {
                showRegistrationStep(0);
                break;
            }
            case R.id.buttonRegisterStudent:
            {
                privileges = "student";
                showRegistrationStep(1);
                break;
            }
            case R.id.registerButtonContinue:
            {
                checkCredentials();
                break;
            }
            case R.id.registerButton:
            {
                register();
                break;
            }
        }
    }

    public void showLoginLayout(View v)
    {
        findViewById(R.id.layoutLogin).setVisibility(View.VISIBLE);
        findViewById(R.id.layoutRegister1).setVisibility(View.INVISIBLE);
        findViewById(R.id.layoutRegister2).setVisibility(View.INVISIBLE);
        findViewById(R.id.layoutRegister3).setVisibility(View.INVISIBLE);
    }

    public void setLoginFields(Map<String,String> map) {

        ((EditText) findViewById(R.id.loginEditTextEmail)).setText(map.get("email"));
        ((EditText) findViewById(R.id.loginEditTextPassword)).setText(map.get("password"));

        String remember = map.get("remember");
        if (remember != null)
            ((CheckBox) findViewById(R.id.loginRememberCheckBox)).setChecked(remember.equals("true"));
    }

    public void tapRememberCheckBox(View v)
    {
        if (!((CheckBox)v).isChecked())
        {
            authSharedPrefs.deleteCredentials();
        }
    }

    private class RegisterReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("result");

            if (result.equals("success"))
            {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.account_create_success),
                        Toast.LENGTH_SHORT).show();

                final String email = intent.getStringExtra("email");
                final String password = intent.getStringExtra("password");

                setLoginFields(new HashMap<String,String>()
                {{
                    put("remember", "true");
                    put("email", email);
                    put("password", password);
                }});

                showLoginLayout(null);
            }
            else
            {
                Toast.makeText(getBaseContext(), result,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class CredentialsCheckReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("result");
            if (result.equals("success")) {
                showRegistrationStep(2);
            }
            else
            {
                Toast.makeText(getBaseContext(), result,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ICallback<LoginResponse> loginCallback = new ICallback<LoginResponse>()
    {
        public void processResponse(final  LoginResponse response)
        {
            if(response.getResult().equals("success"))
            {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.login_success),
                        Toast.LENGTH_SHORT).show();

                if (response.remember)
                {
                    setLoginFields(new HashMap<String,String>()
                    {{
                        put("remember", "true");
                        put("email", response.email);
                        put("password", response.password);
                    }});
                }

                if (response.privileges.equals("student"))
                {
                    Intent studentActivityIntent = new Intent(AuthenticationActivity.this, StudentActivity.class);
                    startActivity(studentActivityIntent);
                }
            }
            else
            {
                Toast.makeText(getBaseContext(), response.getResult(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    };
}
