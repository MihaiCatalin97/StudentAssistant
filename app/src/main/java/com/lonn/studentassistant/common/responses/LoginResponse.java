package com.lonn.studentassistant.common.responses;

import com.lonn.studentassistant.common.abstractions.Response;
import com.lonn.studentassistant.entities.User;

public class LoginResponse extends Response
{
    public User user;
    public String password;
    public boolean remember;

    public LoginResponse(User user, String password, boolean remember)
    {
        super("login", "success");
        this.user = user;
        this.password = password;
        this.remember = remember;
    }

    public LoginResponse(String result)
    {
        super("login", result);
    }
}
