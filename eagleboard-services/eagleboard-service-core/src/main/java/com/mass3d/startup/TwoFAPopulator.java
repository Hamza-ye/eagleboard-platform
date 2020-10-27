package com.mass3d.startup;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.system.startup.AbstractStartupRoutine;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.UserQueryParams;
import com.mass3d.user.UserService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class TwoFAPopulator
    extends AbstractStartupRoutine
{
    private final UserService userService;

    private final CurrentUserService currentUserService;

    public TwoFAPopulator( UserService userService, CurrentUserService currentUserService )
    {
        checkNotNull( userService );
        checkNotNull( currentUserService );
        this.userService = userService;
        this.currentUserService = currentUserService;
    }

    @Override
    public void execute()
        throws Exception
    {
        UserQueryParams userQueryParams = new UserQueryParams( currentUserService.getCurrentUser() );
        userQueryParams.setNot2FA( true );

        userService.getUsers( userQueryParams ).forEach( user -> {
            user.getUserCredentials().setSecret( null );
            userService.updateUser( user );
        } );
    }
}
