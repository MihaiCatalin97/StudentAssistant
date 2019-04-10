package com.lonn.studentassistant.globalServices.userService.dataAccessLayer;

import com.google.firebase.database.FirebaseDatabase;
import com.lonn.studentassistant.common.abstractClasses.AbstractDatabaseController;
import com.lonn.studentassistant.entities.User;

public class UserDatabaseController extends AbstractDatabaseController<User>
{
    public UserDatabaseController()
    {
        super(User.class, null);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");
    }
}
