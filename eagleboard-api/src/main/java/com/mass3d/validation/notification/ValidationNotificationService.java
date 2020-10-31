package com.mass3d.validation.notification;

import java.util.Set;
import com.mass3d.validation.ValidationResult;

public interface ValidationNotificationService
{
    Set<ValidationResult> sendNotifications(Set<ValidationResult> results);

    void sendUnsentNotifications();
}
