package com.mass3d.security;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Implementation of a runnable that makes sure the thread is run in the same
 * security context as the creator, you must implement the call method.
 *
 */
public abstract class SecurityContextRunnable
    implements Runnable
{
    private final SecurityContext securityContext;

    public SecurityContextRunnable()
    {
        this.securityContext = SecurityContextHolder.getContext();
    }

    @Override
    final public void run()
    {
        try
        {
            SecurityContextHolder.setContext( securityContext );
            before();
            call();
        }
        catch ( Throwable ex )
        {
            handleError( ex );
        }
        finally
        {
            after();
            SecurityContextHolder.clearContext();
        }
    }

    public abstract void call();
    
    /**
     * Hook invoked before {@link #call()}.
     */
    public void before()
    {
    }

    /**
     * Hook invoked after {@link #call()}.
     */
    public void after()
    {   
    }

    public void handleError( Throwable ex )
    {
    }
}
