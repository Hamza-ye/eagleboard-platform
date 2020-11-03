package com.mass3d.relationship;

import java.util.Objects;
import com.mass3d.program.Program;
import com.mass3d.system.deletion.DeletionHandler;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.relationship.RelationshipTypeDeletionHandler" )
public class RelationshipTypeDeletionHandler
    extends
    DeletionHandler
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final RelationshipTypeService relationshipTypeService;

    public RelationshipTypeDeletionHandler( RelationshipTypeService relationshipTypeService )
    {
        this.relationshipTypeService = relationshipTypeService;
    }

    // -------------------------------------------------------------------------
    // DeletionHandler implementation
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return RelationshipType.class.getSimpleName();
    }

    @Override
    public void deleteProgram( Program program )
    {
        relationshipTypeService.getAllRelationshipTypes().stream()
            .filter( type -> Objects.equals( type.getFromConstraint().getProgram(), program )
                || Objects.equals( type.getToConstraint().getProgram(), program ) )
            .forEach( relationshipTypeService::deleteRelationshipType );
    }
}
