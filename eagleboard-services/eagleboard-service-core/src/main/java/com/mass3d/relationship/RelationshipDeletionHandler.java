package com.mass3d.relationship;

import java.util.Collection;
import com.mass3d.system.deletion.DeletionHandler;
import com.mass3d.trackedentity.TrackedEntityInstance;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.relationship.RelationshipDeletionHandler" )
public class RelationshipDeletionHandler
    extends DeletionHandler
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final RelationshipService relationshipService;

    public RelationshipDeletionHandler( RelationshipService relationshipService )
    {
        this.relationshipService = relationshipService;
    }

    // -------------------------------------------------------------------------
    // DeletionHandler implementation
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return Relationship.class.getSimpleName();
    }

    @Override
    public void deleteTrackedEntityInstance( TrackedEntityInstance entityInstance )
    {
        Collection<Relationship> relationships = relationshipService
            .getRelationshipsByTrackedEntityInstance( entityInstance, false );

        if ( relationships != null )
        {
            for ( Relationship relationship : relationships )
            {
                relationshipService.deleteRelationship( relationship );
            }
        }
    }

    @Override
    public String allowDeleteRelationshipType( RelationshipType relationshipType )
    {
        Collection<Relationship> relationships = relationshipService.getRelationshipsByRelationshipType( relationshipType );

        return relationships.isEmpty() ? null : ERROR;
    }
}
