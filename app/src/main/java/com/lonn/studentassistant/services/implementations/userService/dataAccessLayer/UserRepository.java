package com.lonn.studentassistant.services.implementations.userService.dataAccessLayer;

import com.lonn.studentassistant.services.abstractions.dataLayer.AbstractRepository;
import com.lonn.studentassistant.entities.User;
import com.lonn.studentassistant.services.implementations.userService.UserService;

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
