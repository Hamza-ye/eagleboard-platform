package com.mass3d.relationship;

import com.mass3d.common.IdentifiableObjectStore;

/**
 * @version $Id$
 */
public interface RelationshipTypeStore
    extends IdentifiableObjectStore<RelationshipType>
{
    String ID = RelationshipTypeStore.class.getName();

    /**
     * Retrieve a relationship
     * 
     * @param aIsToB The A side
     * @param bIsToA The B side
     * 
     * @return RelationshipType
     */
    RelationshipType getRelationshipType(String aIsToB, String bIsToA);

}
