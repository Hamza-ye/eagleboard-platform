package com.mass3d.dxf2.metadata.objectbundle.hooks;

import static com.mass3d.relationship.RelationshipEntity.*;
import static com.mass3d.relationship.RelationshipEntity.PROGRAM_INSTANCE;
import static com.mass3d.relationship.RelationshipEntity.PROGRAM_STAGE_INSTANCE;
import static com.mass3d.relationship.RelationshipEntity.TRACKED_ENTITY_INSTANCE;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.program.Program;
import com.mass3d.program.ProgramService;
import com.mass3d.program.ProgramStage;
import com.mass3d.program.ProgramStageService;
import com.mass3d.relationship.RelationshipConstraint;
import com.mass3d.relationship.RelationshipEntity;
import com.mass3d.relationship.RelationshipType;
import com.mass3d.trackedentity.TrackedEntityType;
import com.mass3d.trackedentity.TrackedEntityTypeService;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.dxf2.metadata.objectbundle.hooks.RelationshipTypeObjectBundleHook" )
public class RelationshipTypeObjectBundleHook
    extends AbstractObjectBundleHook
{
    private final TrackedEntityTypeService trackedEntityTypeService;

    private final ProgramService programService;

    private final ProgramStageService programStageService;

    public RelationshipTypeObjectBundleHook(
        TrackedEntityTypeService trackedEntityTypeService, ProgramService programService,
        ProgramStageService programStageService )
    {
        this.trackedEntityTypeService = trackedEntityTypeService;
        this.programService = programService;
        this.programStageService = programStageService;
    }

    @Override
    public <T extends IdentifiableObject> List<ErrorReport> validate( T object, ObjectBundle bundle )
    {
        if ( !RelationshipType.class.isInstance( object ) )
        {
            return Lists.newArrayList();
        }

        return validateRelationshipType( (RelationshipType) object );
    }

    @Override
    public void preCreate( IdentifiableObject object, ObjectBundle bundle )
    {
        if ( !RelationshipType.class.isInstance( object ) )
        {
            return;
        }

        handleRelationshipTypeReferences( (RelationshipType) object );
    }

    @Override
    public void preUpdate( IdentifiableObject object, IdentifiableObject persistedObject, ObjectBundle bundle )
    {
        if ( !RelationshipType.class.isInstance( object ) )
        {
            return;
        }

        handleRelationshipTypeReferences( (RelationshipType) object );

    }

    /**
     * Handles the references for RelationshipType, persisting any objects that might
     * end up in a transient state.
     *
     * @param relationshipType
     */
    private void handleRelationshipTypeReferences( RelationshipType relationshipType )
    {
        handleRelationshipConstraintReferences( relationshipType.getFromConstraint() );
        handleRelationshipConstraintReferences( relationshipType.getToConstraint() );
    }

    /**
     * Handles the references for RelationshipConstraint, persisting any bject that might
     * end up in a transient state.
     *
     * @param relationshipConstraint
     */
    private void handleRelationshipConstraintReferences( RelationshipConstraint relationshipConstraint )
    {
        TrackedEntityType trackedEntityType = relationshipConstraint.getTrackedEntityType();
        Program program = relationshipConstraint.getProgram();
        ProgramStage programStage = relationshipConstraint.getProgramStage();

        if ( trackedEntityType != null )
        {
            trackedEntityType = trackedEntityTypeService.getTrackedEntityType( trackedEntityType.getUid() );
            relationshipConstraint.setTrackedEntityType( trackedEntityType );
        }

        if ( program != null )
        {
            program = programService.getProgram( program.getUid() );
            relationshipConstraint.setProgram( program );
        }

        if ( programStage != null )
        {
            programStage = programStageService.getProgramStage( programStage.getUid() );
            relationshipConstraint.setProgramStage( programStage );
        }

        sessionFactory.getCurrentSession().save( relationshipConstraint );
    }

    /**
     * Validates the RelationshipType. A type should have constraints for both left and right side.
     * @param relationshipType
     * @return
     */
    private List<ErrorReport> validateRelationshipType( RelationshipType relationshipType )
    {
        ArrayList<ErrorReport> result = new ArrayList<>();

        if ( relationshipType.getFromConstraint() == null )
        {
            result.add( new ErrorReport( RelationshipType.class, ErrorCode.E4000, "leftConstraint" ) );
        }
        else
        {
            result.addAll( validateRelationshipConstraint( relationshipType.getFromConstraint() ) );
        }

        if ( relationshipType.getToConstraint() == null )
        {
            result.add( new ErrorReport( RelationshipType.class, ErrorCode.E4000, "rightConstraint" ) );
        }
        else
        {
            result.addAll( validateRelationshipConstraint( relationshipType.getToConstraint() ) );
        }

        return result;

    }

    /**
     * Validates RelationshipConstraint. Each constraint requires different properties set or not set depending on the
     * RelationshipEntity set for this constraint.
     * @param constraint
     * @return
     */
    private List<ErrorReport> validateRelationshipConstraint( RelationshipConstraint constraint )
    {
        RelationshipEntity entity = constraint.getRelationshipEntity();
        ArrayList<ErrorReport> result = new ArrayList<>();

        if ( entity.equals( TRACKED_ENTITY_INSTANCE ) )
        {

            // Should be not be null
            if ( constraint.getTrackedEntityType() == null )
            {
                result.add( new ErrorReport( RelationshipConstraint.class, ErrorCode.E4024, "trackedEntityType", "relationshipEntity", TRACKED_ENTITY_INSTANCE ) );
            }

            // Should be null
            if ( constraint.getProgramStage() != null )
            {
                result.add( new ErrorReport( RelationshipConstraint.class, ErrorCode.E4023, "programStage", "relationshipEntity", TRACKED_ENTITY_INSTANCE ) );
            }

        }
        else if ( entity.equals( PROGRAM_INSTANCE ) )
        {

            // Should be null
            if ( constraint.getTrackedEntityType() != null )
            {
                result.add( new ErrorReport( RelationshipConstraint.class, ErrorCode.E4023, "trackedEntityType", "relationshipEntity", PROGRAM_INSTANCE ) );
            }

            // Should be not be null
            if ( constraint.getProgram() == null )
            {
                result.add( new ErrorReport( RelationshipConstraint.class, ErrorCode.E4024, "program", "relationshipEntity", PROGRAM_INSTANCE ) );
            }

            // Should be null
            if ( constraint.getProgramStage() != null )
            {
                result.add( new ErrorReport( RelationshipConstraint.class, ErrorCode.E4023, "programStage", "relationshipEntity", PROGRAM_INSTANCE ) );
            }

        }
        else if ( entity.equals( PROGRAM_STAGE_INSTANCE ) )
        {

            // Should be null
            if ( constraint.getTrackedEntityType() != null )
            {
                result.add( new ErrorReport( RelationshipConstraint.class, ErrorCode.E4023, "trackedEntityType", "relationshipEntity", PROGRAM_STAGE_INSTANCE ) );
            }

            // Should be null
            if ( constraint.getProgram() != null && constraint.getProgramStage() != null )
            {
                result.add( new ErrorReport( RelationshipConstraint.class, ErrorCode.E4025, "program", "programStage" ) );
            }

            // Should be null
            if ( constraint.getProgram() == null && constraint.getProgramStage() == null )
            {
                result.add( new ErrorReport( RelationshipConstraint.class, ErrorCode.E4026, "program", "programStage", "relationshipEntity", PROGRAM_STAGE_INSTANCE ) );
            }

        }

        return result;
    }

}
