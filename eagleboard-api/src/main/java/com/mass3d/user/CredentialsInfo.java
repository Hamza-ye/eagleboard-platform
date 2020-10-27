package com.mass3d.user;

public class CredentialsInfo
{
    private String username;

    private String password;

    private String email;

    private boolean newUser;

    protected CredentialsInfo()
    {
    }

    public CredentialsInfo( String username, String password, String email, boolean newUser )
    {
        this.username = username;
        this.password = password;
        this.email = email;
        this.newUser = newUser;
    }

    public CredentialsInfo( String password, boolean newUser )
    {
        this.password = password;
        this.newUser = newUser;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getEmail()
    {
        return email;
    }

    public boolean isNewUser()
    {
        return newUser;
    }
}
