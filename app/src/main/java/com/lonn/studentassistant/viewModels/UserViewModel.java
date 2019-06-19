package com.lonn.studentassistant.viewModels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.firebase.auth.FirebaseUser;

public class UserViewModel extends BaseObservable
{
    @Bindable
    public String email, name;

    public UserViewModel(FirebaseUser user)
    {
        this.email = user.getEmail();
        this.name = user.getDisplayName();
    }
}
