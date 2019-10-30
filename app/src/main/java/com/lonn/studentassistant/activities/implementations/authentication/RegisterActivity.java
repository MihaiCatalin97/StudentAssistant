package com.lonn.studentassistant.activities.implementations.authentication;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.ServiceBoundActivity;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.firebaselayer.models.Student;

public class RegisterActivity extends ServiceBoundActivity {
    private Student registeringStudent;
    private String privileges;

    public void tapRegistrationButton(View v) {
        switch (v.getId()) {
            case R.id.buttonRegisterStudent: {
                privileges = "student";
                showRegistrationStep(1);
                break;
            }
            case R.id.registerButtonContinue: {
                checkCredentials();
                break;
            }
            case R.id.registerButton: {
                register();
                break;
            }
        }
    }

    public void backToLogin(View v) {
        super.onBackPressed();
//        Intent loginActivityIntent = new Intent(this, LoginActivity.class);
//        startActivity(loginActivityIntent);
    }

    protected void inflateLayout() {
        setContentView(R.layout.register_activity_layout);
    }

    private void showRegistrationStep(int step) {
        switch (step) {
            case 0: {
                findViewById(R.id.layoutRegister1).setVisibility(View.INVISIBLE);
                findViewById(R.id.layoutRegister2).setVisibility(View.VISIBLE);
                break;
            }
            case 1: {
                findViewById(R.id.layoutRegister2).setVisibility(View.INVISIBLE);
                findViewById(R.id.layoutRegister3).setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    private void checkCredentials() {
        String numarMatricol = ((EditText) findViewById(R.id.registerNumarMatricolEditText)).getText().toString();
        String firstName = ((EditText) findViewById(R.id.registerFirstNameEditText)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.registerLastNameEditText)).getText().toString();
        String initialaTata = ((EditText) findViewById(R.id.registerFatherEditText)).getText().toString();
        String email = ((EditText) findViewById(R.id.registerEmailEditText)).getText().toString();
        String phoneNumber = ((EditText) findViewById(R.id.registerPhoneEditText)).getText().toString();
        int an;
        String grupa = ((EditText) findViewById(R.id.registerGroupEditText)).getText().toString();

        if (!Utils.isValidStudentId(numarMatricol)) {
            Toast.makeText(getBaseContext(), "Invalid student ID!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            an = Integer.parseInt(((EditText) findViewById(R.id.registerYearEditText)).getText().toString());
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Invalid year!", Toast.LENGTH_SHORT).show();
            return;
        }

        registeringStudent = new Student()
                .setStudentId(numarMatricol)
                .setLastName(lastName)
                .setFirstName(firstName)
                .setFatherInitial(initialaTata)
                .setEmail(email)
                .setPhoneNumber(phoneNumber)
                .setYear(an)
                .setGroup(grupa);

        // save student by hash key and query it at credentials check to protect data integrity
    }

    private void register() {
        String email = ((EditText) findViewById(R.id.registerEditTextEmail)).getText().toString();
        String password = ((EditText) findViewById(R.id.registerEditTextPassword)).getText().toString();
        String password2 = ((EditText) findViewById(R.id.registerEditTextPassword2)).getText().toString();

        if (email.length() == 0) {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() == 0) {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(password2)) {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.invalid_password_match), Toast.LENGTH_SHORT).show();
            return;
        }

        registeringStudent.accountId = email;
    }
}
