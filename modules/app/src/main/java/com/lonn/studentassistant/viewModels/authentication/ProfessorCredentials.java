package com.lonn.studentassistant.viewModels.authentication;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.entities.Professor;

import lombok.ToString;

@ToString
public class ProfessorCredentials extends BaseObservable {
    @Bindable
    public String firstName;
    @Bindable
    public String lastName;
    @Bindable
    public String phoneNumber;

    public Professor toProfessor() {
        return new Professor()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setPhoneNumber(phoneNumber);
    }
}
