package com.lonn.studentassistant.firebaseDatabase.users;

import com.lonn.studentassistant.common.interfaces.IRepository;
import com.lonn.studentassistant.entities.User;

import java.util.List;

public class UserRepository extends IRepository<User>
{
    public UserRepository(UserDatabaseController controller)
    {
        super(controller);
    }

    public User getById(Object id)
    {
        for (User u : items)
        {
            if (u.id.equals(id))
                return u;
        }

        return null;
    }

    public List<User> getAll()
    {
        return items;
    }

    public void update(User user)
    {
        databaseController.update(user);
    }

    public void add(User user)
    {
        databaseController.add(user);
    }

    public void remove(User user)
    {
        databaseController.remove(user);
    }
}
