package com.mass3d.dxf2.metadata.objectbundle.hooks;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.ValueType;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.preheat.PreheatIdentifier;
import com.mass3d.program.ProgramStage;
import com.mass3d.security.acl.AclService;
import org.springframework.stereotype.Component;

@Component
public class ProgramStageObjectBundleHook
    extends AbstractObjectBundleHook
{
    private final AclService aclService;

    public ProgramStageObjectBundleHook( AclService aclService )
    {
        checkNotNull( aclService );
        this.aclService = aclService;
    }

    @Override
    public <T extends IdentifiableObject> List<ErrorReport> validate( T object, ObjectBundle bundle )
    {
        if ( object == null || !object.getClass().isAssignableFrom( ProgramStage.class ) )
        {
            return new ArrayList<>();
        }

        ProgramStage programStage = ( ProgramStage ) object;

        List<ErrorReport> errors = new ArrayList<>();

        if ( programStage.getNextScheduleDate() != null )
        {
            DataElement nextScheduleDate = bundle.getPreheat().get( bundle.getPreheatIdentifier(), DataElement.class,
                programStage.getNextScheduleDate().getUid() );

            if ( !programStage.getDataElements().contains( programStage.getNextScheduleDate() )
                || nextScheduleDate == null || !nextScheduleDate.getValueType().equals( ValueType.DATE ) )
            {
                errors.add( new ErrorReport( ProgramStage.class, ErrorCode.E6001, programStage.getUid(),
                    programStage.getNextScheduleDate().getUid() ) );
            }
        }

        errors.addAll( validateProgramStageDataElementsAcl( programStage, bundle ) );

        return errors;
    }

    @Override
    public <T extends IdentifiableObject> void postCreate( T object, ObjectBundle bundle )
    {
        if ( !ProgramStage.class.isInstance( object ) )
        {
            return;
        }

        ProgramStage programStage = ( ProgramStage ) object;

        Session session = sessionFactory.getCurrentSession();

        updateProgramStageSections( session, programStage );
    }

    private void updateProgramStageSections( Session session, ProgramStage programStage )
    {
        if ( programStage.getProgramStageSections().isEmpty() )
        {
            return;
        }

        programStage.getProgramStageSections().stream().forEach( pss -> {
            if ( pss.getProgramStage() == null )
            {
                pss.setProgramStage( programStage );
            }
        } );

        session.update( programStage );
    }

    private List<ErrorReport> validateProgramStageDataElementsAcl( ProgramStage programStage, ObjectBundle bundle )
    {
        List<ErrorReport> errors = new ArrayList<>();

        if ( programStage.getDataElements().isEmpty() )
        {
            return errors;
        }

        PreheatIdentifier identifier = bundle.getPreheatIdentifier();

        programStage.getDataElements().forEach( de -> {

            DataElement dataElement = bundle.getPreheat().get( identifier, de );

            if ( dataElement == null || !aclService.canRead( bundle.getUser(), de ) )
            {
                errors.add( new ErrorReport( DataElement.class, ErrorCode.E3012, identifier.getIdentifiersWithName( bundle.getUser() ),
                    identifier.getIdentifiersWithName( de ) ) );
            }
        } );

        return errors;
    }
}