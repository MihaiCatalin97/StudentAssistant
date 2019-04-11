package com.lonn.studentassistant.entities.lists;

import com.lonn.studentassistant.entities.User;

import java.util.List;

public class CustomUsersList extends CustomList<User>
{
    public CustomUsersList() {super();}
    public CustomUsersList(List<User> users)
    {
        super(users);
    }
}
