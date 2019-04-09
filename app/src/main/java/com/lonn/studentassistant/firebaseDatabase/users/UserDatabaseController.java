package com.lonn.studentassistant.firebaseDatabase.users;

import com.google.firebase.database.FirebaseDatabase;
import com.lonn.studentassistant.common.interfaces.AbstractDatabaseController;
import com.lonn.studentassistant.entities.User;

public class UserDatabaseController extends AbstractDatabaseController<User>
{
    public UserDatabaseController()
    {
        super(User.class);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");
    }
}
