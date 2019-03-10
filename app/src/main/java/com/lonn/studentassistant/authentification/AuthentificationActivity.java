package com.lonn.studentassistant.authentification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.authentification.dataLayer.AuthService;
import com.lonn.studentassistant.common.DatabasePopulator;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.entities.Student;

import java.util.Map;

public class AuthentificationActivity extends AppCompatActivity {
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity_layout);

        authService = new AuthService(this);

        if (authService.getSharedPreferences().hasSavedCredentials())
            setLoginFields(authService.getSharedPreferences().getCredentials());
    }

    @Override
    public void onStart() {
        super.onStart();
        authService.notifyUserChanged();
    }

    public void login(View v)
    {
        String email = ((EditText)findViewById(R.id.loginEditTextEmail)).getText().toString();
        String password = ((EditText)findViewById(R.id.loginEditTextPassword)).getText().toString();
        boolean remember = ((CheckBox)findViewById(R.id.loginRememberCheckBox)).isChecked();

        authService.login(email, password, remember);
    }

    public void showRegistrationLayout(View v)
    {
        switch (v.getId())
        {
            case R.id.signUpButton:
            {
                findViewById(R.id.layoutLogin).setVisibility(View.INVISIBLE);
                findViewById(R.id.layoutRegister1).setVisibility(View.VISIBLE);
                break;
            }
            case R.id.buttonRegisterStudent:
            {
                findViewById(R.id.layoutRegister1).setVisibility(View.INVISIBLE);
                findViewById(R.id.layoutRegister2).setVisibility(View.VISIBLE);
                break;
            }
            case R.id.registerButtonContinue:
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
                    break;
                }

                try
                {
                    an = Integer.parseInt(((EditText)findViewById(R.id.registerYearEditText)).getText().toString());
                }
                catch (Exception e)
                {
                    Toast.makeText(getBaseContext(), "Invalid year!", Toast.LENGTH_SHORT).show();
                    break;
                }

                authService.credentialsCheck(new Student(numarMatricol, lastName, firstName, initialaTata, email, phoneNumber, an, grupa));

                break;
            }
            case R.id.registerButton:
            {
                String email = ((EditText)findViewById(R.id.registerEditTextEmail)).getText().toString();
                String password = ((EditText)findViewById(R.id.registerEditTextPassword)).getText().toString();

                authService.register(email, password);

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
            authService.getSharedPreferences().deleteCredentials();
        }
    }
}
