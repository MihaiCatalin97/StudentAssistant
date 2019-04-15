package com.lonn.studentassistant.services.userService.dataAccessLayer;

import com.google.firebase.database.FirebaseDatabase;
import com.lonn.studentassistant.common.abstractClasses.AbstractDatabaseController;
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
