package com.mass3d.system.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import com.mass3d.common.IdScheme;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.IdentifiableObjectManager;

public class IdentifiableObjectCallable<T extends IdentifiableObject>
    implements Callable<T>
{
    protected IdentifiableObjectManager manager;
    protected Class<T> clazz;
    protected IdScheme idScheme = IdScheme.UID;
    protected String id;

    public IdentifiableObjectCallable( IdentifiableObjectManager manager, Class<T> clazz, String id )
    {
        this.manager = manager;
        this.clazz = clazz;
        this.id = id;
    }

    public IdentifiableObjectCallable( IdentifiableObjectManager manager, Class<T> clazz, IdScheme idScheme, String id )
    {
        this.manager = manager;
        this.clazz = clazz;
        this.idScheme = idScheme;
        this.id = id;
    }

    @Override
    public T call()
        throws ExecutionException
    {
        return manager.getObject( clazz, idScheme, id );
    }

    public IdentifiableObjectCallable<T> setId( String id )
    {
        this.id = id;
        return this;
    }

    public IdScheme getIdScheme()
    {
        return idScheme;
    }
}
