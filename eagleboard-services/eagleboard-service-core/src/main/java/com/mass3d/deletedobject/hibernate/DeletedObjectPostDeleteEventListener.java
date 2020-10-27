package com.mass3d.deletedobject.hibernate;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.StatelessSession;
import org.hibernate.event.spi.PostCommitDeleteEventListener;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.persister.entity.EntityPersister;
import com.mass3d.common.EmbeddedObject;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.MetadataObject;
import com.mass3d.common.UserContext;
import com.mass3d.deletedobject.DeletedObject;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeletedObjectPostDeleteEventListener implements PostCommitDeleteEventListener
{
    @Override
    public void onPostDelete( PostDeleteEvent event )
    {
        if ( IdentifiableObject.class.isInstance( event.getEntity() )
            && MetadataObject.class.isInstance( event.getEntity() )
            && !EmbeddedObject.class.isInstance( event.getEntity() ) )
        {
            IdentifiableObject identifiableObject = (IdentifiableObject) event.getEntity();
            DeletedObject deletedObject = new DeletedObject( identifiableObject );
            deletedObject.setDeletedBy( getUsername() );

            StatelessSession session = event.getPersister().getFactory().openStatelessSession();
            session.beginTransaction();

            try
            {
                session.insert( deletedObject );
                session.getTransaction().commit();
            }
            catch ( Exception ex )
            {
                log.error( "Failed to save DeletedObject: "+ deletedObject );
                session.getTransaction().rollback();
            }
            finally
            {
                session.close();
            }
        }
    }

    @Override
    public boolean requiresPostCommitHanding( EntityPersister persister )
    {
        return true;
    }

    private String getUsername()
    {
        return UserContext.haveUser() ? UserContext.getUser().getUsername() : "system-process";
    }

    @Override
    public void onPostDeleteCommitFailed( PostDeleteEvent event )
    {
        log.debug( "onPostDeleteCommitFailed: " + event );
    }
}
