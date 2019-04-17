package com.lonn.studentassistant.services.userService.dataAccessLayer;

import com.lonn.studentassistant.common.abstractClasses.AbstractRepository;
import com.lonn.studentassistant.entities.User;
import com.lonn.studentassistant.services.userService.UserService;

public class UserRepository extends AbstractRepository<User>
{
    private static UserRepository instance;

    private UserRepository()
    {
        super(new UserDatabaseController());
    }

    public static UserRepository getInstance(UserService service)
    {
        if (instance == null)
            instance = new UserRepository();

        instance.databaseController.bindService(service);

        return instance;
    }
}
