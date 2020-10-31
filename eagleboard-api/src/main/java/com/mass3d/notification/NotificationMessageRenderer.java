package com.mass3d.notification;

public interface NotificationMessageRenderer<T>
{
    NotificationMessage render(T entity, NotificationTemplate template);
}
