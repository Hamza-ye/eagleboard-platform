package com.mass3d.trackedentity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.organisationunit.OrganisationUnitService;
import com.mass3d.program.Program;
import com.mass3d.program.ProgramService;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.trackedentity.TrackedEntityProgramOwnerService" )
public class DefaultTrackedEntityProgramOwnerService implements TrackedEntityProgramOwnerService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final TrackedEntityInstanceService trackedEntityInstanceService;

    private final ProgramService programService;

    private final OrganisationUnitService orgUnitService;

    private final CurrentUserService currentUserService;

    private final TrackedEntityProgramOwnerStore trackedEntityProgramOwnerStore;

    public DefaultTrackedEntityProgramOwnerService( TrackedEntityInstanceService trackedEntityInstanceService,
        ProgramService programService, OrganisationUnitService orgUnitService, CurrentUserService currentUserService,
        TrackedEntityProgramOwnerStore trackedEntityProgramOwnerStore )
    {
        checkNotNull( trackedEntityInstanceService );
        checkNotNull( programService );
        checkNotNull( orgUnitService );
        checkNotNull( currentUserService );
        checkNotNull( trackedEntityProgramOwnerStore );

        this.trackedEntityInstanceService = trackedEntityInstanceService;
        this.programService = programService;
        this.orgUnitService = orgUnitService;
        this.currentUserService = currentUserService;
        this.trackedEntityProgramOwnerStore = trackedEntityProgramOwnerStore;
    }

    @Override
    @Transactional
    public void createTrackedEntityProgramOwner( String teiUid, String programUid, String orgUnitUid )
    {
        TrackedEntityInstance entityInstance = trackedEntityInstanceService.getTrackedEntityInstance( teiUid );
        if ( entityInstance == null )
        {
            return;
        }
        Program program = programService.getProgram( programUid );
        if ( program == null )
        {
            return;
        }
        OrganisationUnit ou = orgUnitService.getOrganisationUnit( orgUnitUid );
        if ( ou == null )
        {
            return;
        }
        trackedEntityProgramOwnerStore.save( buildTrackedEntityProgramOwner( entityInstance, program, ou ) );
    }

    @Override
    @Transactional
    public void createTrackedEntityProgramOwner( TrackedEntityInstance entityInstance, Program program,
        OrganisationUnit ou )
    {
        if ( entityInstance == null || program == null || ou == null )
        {
            return;
        }
        trackedEntityProgramOwnerStore.save( buildTrackedEntityProgramOwner( entityInstance, program, ou ) );
    }

    private TrackedEntityProgramOwner buildTrackedEntityProgramOwner( TrackedEntityInstance entityInstance,
        Program program, OrganisationUnit ou )
    {
        TrackedEntityProgramOwner teiProgramOwner = new TrackedEntityProgramOwner( entityInstance, program, ou );
        teiProgramOwner.updateDates();
        User user = currentUserService.getCurrentUser();
        if ( user != null )
        {
            teiProgramOwner.setCreatedBy( user.getUsername() );
        }
        return teiProgramOwner;
    }

    @Override
    @Transactional
    public void createOrUpdateTrackedEntityProgramOwner( String teiUid, String programUid, String orgUnitUid )
    {
        TrackedEntityInstance entityInstance = trackedEntityInstanceService.getTrackedEntityInstance( teiUid );
        Program program = programService.getProgram( programUid );
        if ( entityInstance == null )
        {
            return;
        }
        TrackedEntityProgramOwner teiProgramOwner = trackedEntityProgramOwnerStore
            .getTrackedEntityProgramOwner( entityInstance.getId(), program.getId() );
        OrganisationUnit ou = orgUnitService.getOrganisationUnit( orgUnitUid );
        if ( ou == null )
        {
            return;
        }

        if ( teiProgramOwner == null )
        {
            trackedEntityProgramOwnerStore.save( buildTrackedEntityProgramOwner( entityInstance, program, ou ) );
        }
        else
        {
            teiProgramOwner = updateTrackedEntityProgramOwner( teiProgramOwner, ou );
            trackedEntityProgramOwnerStore.update( teiProgramOwner );
        }
    }

    @Override
    @Transactional
    public void createOrUpdateTrackedEntityProgramOwner( long teiUid, long programUid, long orgUnitUid )
    {
        TrackedEntityInstance entityInstance = trackedEntityInstanceService.getTrackedEntityInstance( teiUid );
        Program program = programService.getProgram( programUid );
        if ( entityInstance == null )
        {
            return;
        }
        TrackedEntityProgramOwner teiProgramOwner = trackedEntityProgramOwnerStore
            .getTrackedEntityProgramOwner( entityInstance.getId(), program.getId() );
        OrganisationUnit ou = orgUnitService.getOrganisationUnit( orgUnitUid );
        if ( ou == null )
        {
            return;
        }

        if ( teiProgramOwner == null )
        {
            trackedEntityProgramOwnerStore.save( buildTrackedEntityProgramOwner( entityInstance, program, ou ) );
        }
        else
        {
            teiProgramOwner = updateTrackedEntityProgramOwner( teiProgramOwner, ou );
            trackedEntityProgramOwnerStore.update( teiProgramOwner );
        }
    }

    @Override
    @Transactional
    public void createOrUpdateTrackedEntityProgramOwner( TrackedEntityInstance entityInstance, Program program,
        OrganisationUnit ou )
    {
        if ( entityInstance == null || program == null || ou == null )
        {
            return;
        }
        TrackedEntityProgramOwner teiProgramOwner = trackedEntityProgramOwnerStore
            .getTrackedEntityProgramOwner( entityInstance.getId(), program.getId() );
        if ( teiProgramOwner == null )
        {
            trackedEntityProgramOwnerStore.save( buildTrackedEntityProgramOwner( entityInstance, program, ou ) );
        }
        else
        {
            teiProgramOwner = updateTrackedEntityProgramOwner( teiProgramOwner, ou );
            trackedEntityProgramOwnerStore.update( teiProgramOwner );
        }
    }

    @Override
    @Transactional
    public void updateTrackedEntityProgramOwner( TrackedEntityInstance entityInstance, Program program,
        OrganisationUnit ou )
    {
        if ( entityInstance == null || program == null || ou == null )
        {
            return;
        }
        TrackedEntityProgramOwner teiProgramOwner = trackedEntityProgramOwnerStore
            .getTrackedEntityProgramOwner( entityInstance.getId(), program.getId() );
        if ( teiProgramOwner == null )
        {
            return;
        }
        teiProgramOwner = updateTrackedEntityProgramOwner( teiProgramOwner, ou );
        trackedEntityProgramOwnerStore.update( teiProgramOwner );
    }

    private TrackedEntityProgramOwner updateTrackedEntityProgramOwner( TrackedEntityProgramOwner teiProgramOwner,
        OrganisationUnit ou )
    {
        teiProgramOwner.setOrganisationUnit( ou );
        teiProgramOwner.updateDates();
        User user = currentUserService.getCurrentUser();
        if ( user != null )
        {
            teiProgramOwner.setCreatedBy( user.getUsername() );
        }
        return teiProgramOwner;
    }

    @Override
    @Transactional
    public void updateTrackedEntityProgramOwner( String teiUid, String programUid, String orgUnitUid )
    {
        TrackedEntityInstance entityInstance = trackedEntityInstanceService.getTrackedEntityInstance( teiUid );
        if ( entityInstance == null )
        {
            return;
        }
        Program program = programService.getProgram( programUid );
        if ( program == null )
        {
            return;
        }

        TrackedEntityProgramOwner teProgramOwner = trackedEntityProgramOwnerStore
            .getTrackedEntityProgramOwner( entityInstance.getId(), program.getId() );
        if ( teProgramOwner == null )
        {
            return;
        }
        OrganisationUnit ou = orgUnitService.getOrganisationUnit( orgUnitUid );
        if ( ou == null )
        {
            return;
        }
        teProgramOwner = updateTrackedEntityProgramOwner( teProgramOwner, ou );
        trackedEntityProgramOwnerStore.update( teProgramOwner );
    }

    @Override
    @Transactional
    public void createTrackedEntityProgramOwner( long teiId, long programId, long orgUnitId )
    {
        TrackedEntityInstance entityInstance = trackedEntityInstanceService.getTrackedEntityInstance( teiId );
        if ( entityInstance == null )
        {
            return;
        }
        Program program = programService.getProgram( programId );
        if ( program == null )
        {
            return;
        }
        OrganisationUnit ou = orgUnitService.getOrganisationUnit( orgUnitId );
        if ( ou == null )
        {
            return;
        }
        trackedEntityProgramOwnerStore.save( buildTrackedEntityProgramOwner( entityInstance, program, ou ) );
    }

    @Override
    @Transactional
    public void updateTrackedEntityProgramOwner( long teiId, long programId, long orgUnitId )
    {
        TrackedEntityProgramOwner teProgramOwner = trackedEntityProgramOwnerStore.getTrackedEntityProgramOwner( teiId,
            programId );
        if ( teProgramOwner == null )
        {
            return;
        }
        OrganisationUnit ou = orgUnitService.getOrganisationUnit( orgUnitId );
        if ( ou == null )
        {
            return;
        }
        trackedEntityProgramOwnerStore.update( updateTrackedEntityProgramOwner( teProgramOwner, ou ) );
    }

    @Override
    @Transactional(readOnly = true)
    public TrackedEntityProgramOwner getTrackedEntityProgramOwner( long teiId, long programId )
    {
        return trackedEntityProgramOwnerStore.getTrackedEntityProgramOwner( teiId, programId );
    }

    @Override
    @Transactional(readOnly = true)
    public TrackedEntityProgramOwner getTrackedEntityProgramOwner( String teiUid, String programUid )
    {
        TrackedEntityInstance entityInstance = trackedEntityInstanceService.getTrackedEntityInstance( teiUid );
        Program program = programService.getProgram( programUid );
        if ( entityInstance == null || program == null )
        {
            return null;
        }
        return trackedEntityProgramOwnerStore.getTrackedEntityProgramOwner( entityInstance.getId(), program.getId() );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackedEntityProgramOwner> getTrackedEntityProgramOwnersUsingId( List<Long> teiIds )
    {
        return trackedEntityProgramOwnerStore.getTrackedEntityProgramOwners( teiIds );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackedEntityProgramOwner> getTrackedEntityProgramOwnersUsingId( List<Long> teiIds, Program program )
    {
        return trackedEntityProgramOwnerStore.getTrackedEntityProgramOwners( teiIds, program.getId() );
    }

}
