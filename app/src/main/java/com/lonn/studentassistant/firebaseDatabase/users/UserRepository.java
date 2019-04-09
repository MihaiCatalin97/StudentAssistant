package com.lonn.studentassistant.firebaseDatabase.users;

import com.lonn.studentassistant.common.interfaces.AbstractRepository;
import com.lonn.studentassistant.entities.User;

public class UserRepository extends AbstractRepository<User>
{
    public UserRepository(UserDatabaseController controller)
    {
        super(controller);
    }
}
