package com.lonn.studentassistant.firebaseDatabase.users;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lonn.studentassistant.common.Utils;
import com.lonn.studentassistant.common.interfaces.IDatabaseController;
import com.lonn.studentassistant.entities.User;

import java.util.List;

public class UserDatabaseController implements IDatabaseController<User>
{
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public UserDatabaseController()
    {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");
    }

    public void setAll(List<User> items)
    {
    }

    public void add(User user)
    {
        databaseReference.child(Utils.convertEmail(user.email)).setValue(user);
    }

    public void remove(User user)
    {
        databaseReference.child(Utils.convertEmail(user.email)).removeValue();
    }

    public void update(User user)
    {
        databaseReference.child(Utils.convertEmail(user.email)).setValue(user);
    }
}
