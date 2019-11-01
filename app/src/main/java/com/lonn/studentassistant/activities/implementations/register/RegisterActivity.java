package com.lonn.studentassistant.activities.implementations.register;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lonn.studentassistant.R;
import com.lonn.studentassistant.activities.abstractions.ServiceBoundActivity;
import com.lonn.studentassistant.activities.implementations.login.LoginActivity;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.config.UserPrivileges;
import com.lonn.studentassistant.firebaselayer.models.Student;
import com.lonn.studentassistant.firebaselayer.predicates.Predicate;
import com.lonn.studentassistant.firebaselayer.predicates.fields.StudentFields;
import com.lonn.studentassistant.firebaselayer.requests.DatabaseTable;
import com.lonn.studentassistant.firebaselayer.requests.GetRequest;

public class RegisterActivity extends ServiceBoundActivity {
    private Student registeringStudent;
    private UserPrivileges privileges;

    public void tapRegistrationAccountTypeButton(View v) {
        switch (v.getId()) {
            case R.id.buttonRegisterStudent: {
                privileges = UserPrivileges.STUDENT;
                showRegistrationStep(0);
                break;
            }
            case R.id.buttonRegisterProfessor: {
                privileges = UserPrivileges.PROFESSOR;
                showRegistrationStep(0);
                break;
            }
            case R.id.buttonRegisterAdministrator: {
                privileges = UserPrivileges.ADMIN;
                showRegistrationStep(0);
                break;
            }
        }
    }

    public void tapStudentCredentialsCheckButton(View v) {
        checkStudentCredentials();
    }

    public void tapCreateAccountButton(View v) {
        register();
    }

    public void backToLogin(View v) {
        super.onBackPressed();

        Intent loginActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(loginActivityIntent);
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

    private void checkStudentCredentials() {
        String studentId = ((EditText) findViewById(R.id.registerStudentIdEditText)).getText().toString();
        String firstName = ((EditText) findViewById(R.id.registerFirstNameEditText)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.registerLastNameEditText)).getText().toString();
        String fatherInitial = ((EditText) findViewById(R.id.registerFatherInitialEditText)).getText().toString();
        String email = ((EditText) findViewById(R.id.registerEmailEditText)).getText().toString();
        String phoneNumber = ((EditText) findViewById(R.id.registerPhoneEditText)).getText().toString();
        int year;
        String group = ((EditText) findViewById(R.id.registerGroupEditText)).getText().toString();

        if (!Utils.isValidStudentId(studentId)) {
            Toast.makeText(getBaseContext(), "Invalid student ID!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            year = Integer.parseInt(((EditText) findViewById(R.id.registerYearEditText)).getText().toString());
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Invalid year!", Toast.LENGTH_SHORT).show();
            return;
        }

        registeringStudent = new Student()
                .setStudentId(studentId)
                .setLastName(lastName)
                .setFirstName(firstName)
                .setFatherInitial(fatherInitial)
                .setEmail(email)
                .setPhoneNumber(phoneNumber)
                .setYear(year)
                .setGroup(group);

        firebaseConnection.execute(new GetRequest<Student>()
                .databaseTable(DatabaseTable.STUDENTS)
                .onSuccess((students) -> {
                    if (students.size() == 1) {
                        showSnackBar("Student identified successful!", 1000);
                        hideKeyboard();
                        //TODO: Store student id and advance
                    }
                })
                .onError((error) -> {
                    showSnackBar(error.getMessage(), 1000);
                })
                .predicate(Predicate.where(StudentFields.IDENTIFICATION_HASH)
                        .equalTo(registeringStudent.getIdentificationHash())));
    }

    private void register() {
        String email = ((EditText) findViewById(R.id.registerEditTextEmail)).getText().toString();
        String password = ((EditText) findViewById(R.id.registerEditTextPassword)).getText().toString();
        String repeatPassword = ((EditText) findViewById(R.id.registerEditTextRepeatPassword)).getText().toString();

        if (email.length() == 0) {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() == 0) {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(repeatPassword)) {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.invalid_password_match), Toast.LENGTH_SHORT).show();
            return;
        }

        registeringStudent.setAccountId(email);
    }
}
