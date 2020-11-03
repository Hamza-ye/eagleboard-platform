package com.mass3d.relationship;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Optional;
import com.mass3d.program.ProgramInstance;
import com.mass3d.program.ProgramStageInstance;
import com.mass3d.trackedentity.TrackedEntityInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.relationship.RelationshipService" )
public class DefaultRelationshipService
    implements RelationshipService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final RelationshipStore relationshipStore;

    public DefaultRelationshipService( RelationshipStore relationshipStore )
    {
        checkNotNull( relationshipStore );

        this.relationshipStore = relationshipStore;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public void deleteRelationship( Relationship relationship )
    {
        relationshipStore.delete( relationship );
    }

    @Override
    @Transactional(readOnly = true)
    public Relationship getRelationship( long id )
    {
        return relationshipStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean relationshipExists( String uid )
    {
        return relationshipStore.getByUid( uid ) != null;
    }

    @Override
    @Transactional
    public long addRelationship( Relationship relationship )
    {
        relationship.getFrom().setRelationship( relationship );
        relationship.getTo().setRelationship( relationship );
        relationshipStore.save( relationship );

        return relationship.getId();
    }

    @Override
    @Transactional
    public void updateRelationship( Relationship relationship )
    {
        //TODO: Do we need next 2 lines? relationship never changes during update
        relationship.getFrom().setRelationship( relationship );
        relationship.getTo().setRelationship( relationship );
        relationshipStore.update( relationship );
    }

    @Override
    @Transactional(readOnly = true)
    public Relationship getRelationship( String uid )
    {
        return relationshipStore.getByUid( uid );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Relationship> getRelationshipsByTrackedEntityInstance( TrackedEntityInstance tei,
        boolean skipAccessValidation )
    {
        return relationshipStore.getByTrackedEntityInstance( tei );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Relationship> getRelationshipsByProgramInstance( ProgramInstance pi, boolean skipAccessValidation )
    {
        return relationshipStore.getByProgramInstance( pi );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Relationship> getRelationshipsByProgramStageInstance( ProgramStageInstance psi,
        boolean skipAccessValidation )
    {
        return relationshipStore.getByProgramStageInstance( psi );
    }

    @Override
    @Transactional( readOnly = true )
    public List<Relationship> getRelationshipsByRelationshipType( RelationshipType relationshipType )
    {
        return relationshipStore.getByRelationshipType( relationshipType );
    }

    @Override
    @Transactional( readOnly = true )
    public Optional<Relationship> getRelationshipByRelationship( Relationship relationship )
    {
        checkNotNull( relationship.getFrom() );
        checkNotNull( relationship.getTo() );
        checkNotNull( relationship.getRelationshipType() );

        return Optional.ofNullable( relationshipStore.getByRelationship( relationship ) );
    }
}
