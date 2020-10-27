package com.mass3d.system.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.IdentifiableObjectManager;

public class IdentifiableObjectSearchCallable<T extends IdentifiableObject>
    implements Callable<T>
{
    protected IdentifiableObjectManager manager;
    protected Class<T> clazz;
    protected String id;

    public IdentifiableObjectSearchCallable( IdentifiableObjectManager manager, Class<T> clazz, String id )
    {
        this.manager = manager;
        this.clazz = clazz;
        this.id = id;
    }

    @Override
    public T call()
        throws ExecutionException
    {
        return manager.search( clazz, id );
    }

    public IdentifiableObjectSearchCallable<T> setId( String id )
    {
        this.id = id;
        return this;
    }
}
