package com.mass3d.artemis.config;

import java.util.Optional;
import java.util.function.Supplier;
import com.mass3d.user.CurrentUserService;
import org.springframework.stereotype.Component;

@Component
public class UsernameSupplier implements Supplier<String>
{
    private final static String DEFAULT_USERNAME = "system-process"; // TODO this may come from configuration

    private final CurrentUserService currentUserService;

    public UsernameSupplier( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    @Override
    public String get()
    {
        return Optional.ofNullable( currentUserService.getCurrentUsername() ).orElse( DEFAULT_USERNAME );
    }
}
