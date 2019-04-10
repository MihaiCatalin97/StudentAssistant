package com.lonn.studentassistant.globalServices.userService;

import com.lonn.studentassistant.common.abstractClasses.LocalService;
import com.lonn.studentassistant.entities.User;
import com.lonn.studentassistant.globalServices.userService.dataAccessLayer.UserDatabaseController;
import com.lonn.studentassistant.globalServices.userService.dataAccessLayer.UserRepository;

public class UserService extends LocalService<User>
{
    public void getAll()
    {
        if (serviceCallbacks != null && repository.getAll() != null)
            serviceCallbacks.resultGetAll(repository.getAll());
    }

    public UserRepository instantiateRepository()
    {
        return new UserRepository(new UserDatabaseController());
    }
}
