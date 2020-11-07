package com.mass3d.dxf2.metadata.objectbundle.hooks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.preheat.PreheatIdentifier;
import com.mass3d.program.*;
import com.mass3d.security.acl.AccessStringHelper;
import com.mass3d.security.acl.AclService;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import org.springframework.stereotype.Component;

@Component
public class ProgramObjectBundleHook
    extends
    AbstractObjectBundleHook
{
    private final ProgramInstanceService programInstanceService;

    private final ProgramService programService;

    private final ProgramStageService programStageService;

    private final AclService aclService;

    public ProgramObjectBundleHook( ProgramInstanceService programInstanceService, ProgramService programService,
        ProgramStageService programStageService, AclService aclService )
    {
        this.programInstanceService = programInstanceService;
        this.programStageService = programStageService;
        this.programService = programService;
        this.aclService = aclService;
    }

    @Override
    public void postCreate( IdentifiableObject object, ObjectBundle bundle )
    {
        if ( !isProgram( object ) )
        {
            return;
        }

        syncSharingForEventProgram( (Program) object );

        addProgramInstance( (Program) object );

        updateProgramStage( (Program) object );
    }

    @Override
    public void postUpdate( IdentifiableObject object, ObjectBundle bundle )
    {
        if ( !isProgram( object ) )
        {
            return;
        }

        syncSharingForEventProgram( (Program) object );
    }

    @Override
    public <T extends IdentifiableObject> List<ErrorReport> validate( T object, ObjectBundle bundle )
    {
        List<ErrorReport> errors = new ArrayList<>();

        if ( !isProgram( object ) )
        {
            return errors;
        }

        Program program = (Program) object;

        if ( program.getId() != 0 && getProgramInstancesCount( program ) > 1 )
        {
            errors.add( new ErrorReport( Program.class, ErrorCode.E6000, program.getName() ) );
        }

        errors.addAll( validateAttributeSecurity( program, bundle ) );

        return errors;
    }

    private void syncSharingForEventProgram( Program program )
    {
        if ( ProgramType.WITHOUT_REGISTRATION != program.getProgramType() || program.getProgramStages().isEmpty() )
        {
            return;
        }

        ProgramStage programStage = program.getProgramStages().iterator().next();
        AccessStringHelper.copySharing( program, programStage );

        programStage.setUser( program.getUser() );
        programStageService.updateProgramStage( programStage );
    }

    private void updateProgramStage( Program program )
    {
        if ( program.getProgramStages().isEmpty() )
        {
            return;
        }

        program.getProgramStages().stream().forEach( ps -> {

            if ( Objects.isNull( ps.getProgram() ) )
            {
                ps.setProgram( program );
            }

            programStageService.saveProgramStage( ps );
        } );

        programService.updateProgram( program );
    }

    private void addProgramInstance( Program program )
    {
        if ( getProgramInstancesCount( program ) == 0 && program.isWithoutRegistration() )
        {
            ProgramInstance pi = new ProgramInstance();
            pi.setEnrollmentDate( new Date() );
            pi.setIncidentDate( new Date() );
            pi.setProgram( program );
            pi.setStatus( ProgramStatus.ACTIVE );
            pi.setStoredBy( "system-process" );

            this.programInstanceService.addProgramInstance( pi );
        }
    }

    private int getProgramInstancesCount( Program program )
    {
        return programInstanceService.getProgramInstances( program, ProgramStatus.ACTIVE ).size();
    }

    private boolean isProgram( Object object )
    {
        return object instanceof Program;
    }

    private List<ErrorReport> validateAttributeSecurity( Program program, ObjectBundle bundle )
    {
        List<ErrorReport> errorReports = new ArrayList<>();

        if ( program.getProgramAttributes().isEmpty() )
        {
            return errorReports;
        }

        PreheatIdentifier identifier = bundle.getPreheatIdentifier();

        program.getProgramAttributes().forEach( programAttr -> {
            TrackedEntityAttribute attribute = bundle.getPreheat().get( identifier, programAttr.getAttribute() );

            if ( attribute == null || !aclService.canRead( bundle.getUser(), attribute ) )
            {
                errorReports.add( new ErrorReport( TrackedEntityAttribute.class, ErrorCode.E3012,
                    identifier.getIdentifiersWithName( bundle.getUser() ),
                    identifier.getIdentifiersWithName( programAttr.getAttribute() ) ) );
            }
        } );

        return errorReports;
    }
}
