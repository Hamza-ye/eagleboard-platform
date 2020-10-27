package com.mass3d.deletedobject;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.deletedobject.DeletedObjectService" )
public class DefaultDeletedObjectService
    implements DeletedObjectService
{
    private final DeletedObjectStore deletedObjectStore;

    public DefaultDeletedObjectService( DeletedObjectStore deletedObjectStore )
    {
        checkNotNull( deletedObjectStore );
        this.deletedObjectStore = deletedObjectStore;
    }

    @Override
    @Transactional
    public void addDeletedObject( DeletedObject deletedObject )
    {
        deletedObjectStore.save( deletedObject );
    }

    @Override
    @Transactional
    public void deleteDeletedObject( DeletedObject deletedObject )
    {
        deletedObjectStore.delete( deletedObject );
    }

    @Override
    @Transactional
    public void deleteDeletedObjects( DeletedObjectQuery query )
    {
        deletedObjectStore.delete( query );
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeletedObject> getDeletedObjectsByKlass( String klass )
    {
        return deletedObjectStore.getByKlass( klass );
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeletedObject> getDeletedObjects()
    {
        return deletedObjectStore.query( DeletedObjectQuery.EMPTY );
    }

    @Override
    @Transactional
    public int countDeletedObjects()
    {
        return deletedObjectStore.count( DeletedObjectQuery.EMPTY );
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeletedObject> getDeletedObjects( DeletedObjectQuery query )
    {
        return deletedObjectStore.query( query );
    }

    @Override
    @Transactional(readOnly = true)
    public int countDeletedObjects( DeletedObjectQuery query )
    {
        return deletedObjectStore.count( query );
    }
}
