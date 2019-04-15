package com.lonn.studentassistant.common.requests;

public class LoginRequest extends Request
{
    public String email;
    public String password;
    public boolean remember;

    public LoginRequest(String email, String password, boolean remember)
    {
        super("login");

        this.email = email;
        this.password = password;
        this.remember = remember;
    }
}
