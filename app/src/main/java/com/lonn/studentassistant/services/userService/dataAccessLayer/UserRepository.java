package com.lonn.studentassistant.services.userService.dataAccessLayer;

import com.lonn.studentassistant.common.abstractClasses.AbstractRepository;
import com.lonn.studentassistant.entities.User;

public class UserRepository extends AbstractRepository<User>
{
    public UserRepository(UserDatabaseController controller)
    {
        super(controller);
    }
}
