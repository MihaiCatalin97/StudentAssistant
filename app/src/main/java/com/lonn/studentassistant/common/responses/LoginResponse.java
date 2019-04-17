package com.lonn.studentassistant.common.responses;

import com.lonn.studentassistant.common.abstractions.Response;

public class LoginResponse extends Response
{
    public String email;
    public String password;
    public boolean remember;
    public String privileges;

    public LoginResponse(String email, String password, boolean remember, String privileges)
    {
        super("login", "success");
        this.email = email;
        this.password = password;
        this.remember = remember;
        this.privileges = privileges;
    }

    public LoginResponse(String result)
    {
        super("login", result);
    }
}
