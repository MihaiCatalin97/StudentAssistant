package com.lonn.studentassistant.services.implementations.userService;

import com.lonn.studentassistant.notifications.abstractions.NotificationCreator;
import com.lonn.studentassistant.services.abstractions.DatabaseService;
import com.lonn.studentassistant.entities.User;
import com.lonn.studentassistant.services.implementations.userService.dataAccessLayer.UserRepository;

public class UserService extends DatabaseService<User>
{
    public UserRepository instantiateRepository()
    {
        return UserRepository.getInstance(this);
    }

    public NotificationCreator<User> instantiateCreator()
    {
        return null;
    }
}
