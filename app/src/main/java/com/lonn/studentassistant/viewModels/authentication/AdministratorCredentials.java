package com.lonn.studentassistant.viewModels.authentication;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.lonn.studentassistant.firebaselayer.models.Administrator;

import lombok.ToString;

@ToString
public class AdministratorCredentials extends BaseObservable {
    @Bindable
    public String firstName;
    @Bindable
    public String lastName;
    @Bindable
    public String administratorKey;

    public Administrator toAdministrator() {
        return new Administrator()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setAdministratorKey(administratorKey);
    }
}
