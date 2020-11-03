package com.mass3d.relationship;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.relationship.RelationshipTypeService" )
public class DefaultRelationshipTypeService
    implements RelationshipTypeService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final RelationshipTypeStore relationshipTypeStore;

    public DefaultRelationshipTypeService( RelationshipTypeStore relationshipTypeStore )
    {
        checkNotNull( relationshipTypeStore );
        this.relationshipTypeStore = relationshipTypeStore;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public void deleteRelationshipType( RelationshipType relationshipType )
    {
        relationshipTypeStore.delete( relationshipType );
    }

    @Override
    @Transactional(readOnly = true)
    public List<RelationshipType> getAllRelationshipTypes()
    {
        return relationshipTypeStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public RelationshipType getRelationshipType( long id )
    {
        return relationshipTypeStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public RelationshipType getRelationshipType( String uid )
    {
        return relationshipTypeStore.getByUid( uid );
    }

    @Override
    @Transactional
    public long addRelationshipType( RelationshipType relationshipType )
    {
        relationshipTypeStore.save( relationshipType );
        return relationshipType.getId();
    }

    @Override
    @Transactional
    public void updateRelationshipType( RelationshipType relationshipType )
    {
        relationshipTypeStore.update( relationshipType );
    }

    @Override
    @Transactional(readOnly = true)
    public RelationshipType getRelationshipType( String aIsToB, String bIsToA )
    {
        return relationshipTypeStore.getRelationshipType( aIsToB, bIsToA );
    }
}
