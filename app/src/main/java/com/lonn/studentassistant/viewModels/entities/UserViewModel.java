package com.lonn.studentassistant.viewModels.entities;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.firebase.auth.FirebaseUser;

public class UserViewModel extends BaseObservable {
    @Bindable
    public String email, name;

    public UserViewModel(FirebaseUser user) {
        this.email = user.getEmail();
        this.name = user.getDisplayName();
    }
}
