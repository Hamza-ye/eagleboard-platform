package com.mass3d;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

public class ProxyUtils
{
    public static <T> T getProxyTarget( Object proxy )
    {
        if ( !AopUtils.isAopProxy( proxy ) )
        {
            throw new IllegalStateException( "Target must be a proxy" );
        }

        TargetSource targetSource = ((Advised) proxy).getTargetSource();
        return getTarget( targetSource );
    }

    @SuppressWarnings("unchecked")
    private static <T> T getTarget( TargetSource targetSource )
    {
        try
        {
            return (T) targetSource.getTarget();
        }
        catch ( Exception e )
        {
            throw new IllegalStateException( e );
        }
    }
}
