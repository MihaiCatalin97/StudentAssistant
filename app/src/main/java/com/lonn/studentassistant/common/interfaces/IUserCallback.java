package com.lonn.studentassistant.common.interfaces;

import com.lonn.studentassistant.entities.lists.CustomUsersList;
import com.lonn.studentassistant.entities.User;

public interface IUserCallback extends IServiceCallback
{
    void resultGetById(User item);
    void resultGetAll(CustomUsersList items);
}
