package com.lonn.studentassistant.services.implementations.userService.dataAccessLayer;

import com.google.firebase.database.FirebaseDatabase;
import com.lonn.studentassistant.services.abstractions.dataLayer.AbstractDatabaseController;
import com.lonn.studentassistant.entities.User;

class UserDatabaseController extends AbstractDatabaseController<User>
{
    UserDatabaseController()
    {
        super(User.class);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");
    }
}
