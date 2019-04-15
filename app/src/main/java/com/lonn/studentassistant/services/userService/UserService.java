package com.lonn.studentassistant.services.userService;

import com.lonn.studentassistant.common.abstractClasses.LocalService;
import com.lonn.studentassistant.entities.User;
import com.lonn.studentassistant.services.userService.dataAccessLayer.UserRepository;

public class UserService extends LocalService<User>
{
    public UserRepository instantiateRepository()
    {
        return UserRepository.getInstance(this);
    }
}
