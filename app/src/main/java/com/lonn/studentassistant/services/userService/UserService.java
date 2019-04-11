package com.lonn.studentassistant.services.userService;

import com.lonn.studentassistant.entities.lists.CustomUsersList;
import com.lonn.studentassistant.common.abstractClasses.LocalService;
import com.lonn.studentassistant.common.interfaces.IServiceCallback;
import com.lonn.studentassistant.common.interfaces.IUserCallback;
import com.lonn.studentassistant.entities.User;
import com.lonn.studentassistant.services.userService.dataAccessLayer.UserDatabaseController;
import com.lonn.studentassistant.services.userService.dataAccessLayer.UserRepository;

public class UserService extends LocalService<User>
{
    public void getAll()
    {
        if (repository.getAll() != null)
            for (IServiceCallback callback : serviceCallbacks)
                ((IUserCallback)callback).resultGetAll(new CustomUsersList(repository.getAll()));
    }

    public UserRepository instantiateRepository()
    {
        return new UserRepository(new UserDatabaseController());
    }
}
