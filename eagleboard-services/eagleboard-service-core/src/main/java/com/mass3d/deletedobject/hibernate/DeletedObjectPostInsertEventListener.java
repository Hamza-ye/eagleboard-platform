package com.mass3d.deletedobject.hibernate;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.StatelessSession;
import org.hibernate.event.spi.PostCommitInsertEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.persister.entity.EntityPersister;
import com.mass3d.common.EmbeddedObject;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.MetadataObject;
import com.mass3d.deletedobject.DeletedObject;
import com.mass3d.deletedobject.DeletedObjectQuery;
import com.mass3d.deletedobject.DeletedObjectService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeletedObjectPostInsertEventListener
    implements PostCommitInsertEventListener
{
    private final DeletedObjectService deletedObjectService;

    public DeletedObjectPostInsertEventListener( DeletedObjectService deletedObjectService )
    {
        checkNotNull( deletedObjectService );
        this.deletedObjectService = deletedObjectService;
    }

    @Override
    public boolean requiresPostCommitHanding( EntityPersister persister )
    {
        return true;
    }

    @Override
    public void onPostInsert( PostInsertEvent event )
    {
        if ( IdentifiableObject.class.isInstance( event.getEntity() )
            && MetadataObject.class.isInstance( event.getEntity() )
            && !EmbeddedObject.class.isInstance( event.getEntity() ) )
        {
            StatelessSession session = event.getPersister().getFactory().openStatelessSession();
            session.beginTransaction();

            try
            {
                List<DeletedObject> deletedObjects = deletedObjectService
                    .getDeletedObjects( new DeletedObjectQuery( (IdentifiableObject) event.getEntity() ) );

                deletedObjects.forEach( deletedObject -> session.delete( deletedObject ) );

                session.getTransaction().commit();
            }
            catch ( Exception ex )
            {
                log.error( "Failed to delete DeletedObject for:" + event.getEntity() );
                session.getTransaction().rollback();
            }
            finally
            {
                session.close();
            }
        }
    }

    @Override
    public void onPostInsertCommitFailed( PostInsertEvent event )
    {
        log.debug( "onPostInsertCommitFailed: " + event );
    }
}
