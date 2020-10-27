package com.mass3d.security;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Implementation of a runnable that makes sure the thread is run without
 * any security context (user = null). Useful for cases where you want to have
 * access to all objects without the user flag interfering.
 *
 */
public abstract class NoSecurityContextRunnable
    implements Runnable
{
    @Override
    final public void run()
    {
        SecurityContextHolder.clearContext();
        call();
    }

    public abstract void call();
}
