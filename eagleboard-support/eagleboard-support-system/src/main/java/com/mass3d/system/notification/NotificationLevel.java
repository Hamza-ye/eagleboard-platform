package com.mass3d.system.notification;

public enum NotificationLevel
{
    OFF,
    DEBUG,
    INFO,
    WARN,
    ERROR;
    
    public boolean isOff()
    {
        return this == OFF;
    }
}
