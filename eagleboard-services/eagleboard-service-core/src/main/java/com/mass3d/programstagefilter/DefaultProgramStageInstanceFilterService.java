package com.mass3d.programstagefilter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import com.mass3d.common.AssignedUserSelectionMode;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.organisationunit.OrganisationUnitService;
import com.mass3d.program.Program;
import com.mass3d.program.ProgramService;
import com.mass3d.program.ProgramStage;
import com.mass3d.program.ProgramStageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.programstagefilter.ProgramStageInstanceFilterService" )
@Transactional(readOnly = true)
public class DefaultProgramStageInstanceFilterService implements ProgramStageInstanceFilterService
{

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ProgramService programService;

    private ProgramStageService programStageService;

    private OrganisationUnitService organisationUnitService;

    public DefaultProgramStageInstanceFilterService( ProgramService programService, ProgramStageService programStageService,
        OrganisationUnitService organisationUnitService )
    {
        this.programService = programService;
        this.programStageService = programStageService;
        this.organisationUnitService = organisationUnitService;
    }

    // -------------------------------------------------------------------------
    // ProgramStageInstanceFilterService implementation
    // -------------------------------------------------------------------------

    @Override
    public List<String> validate( ProgramStageInstanceFilter programStageInstanceFilter )
    {
        List<String> errors = new ArrayList<>();

        if ( programStageInstanceFilter.getProgram() == null )
        {
            errors.add( "Program should be specified for event filters." );
        }
        else
        {
            Program pr = programService.getProgram( programStageInstanceFilter.getProgram() );

            if ( pr == null )
            {
                errors.add( "Program is specified but does not exist: " + programStageInstanceFilter.getProgram() );
            }
        }

        if ( programStageInstanceFilter.getProgramStage() != null )
        {
            ProgramStage ps = programStageService.getProgramStage( programStageInstanceFilter.getProgramStage() );
            if ( ps == null )
            {
                errors.add( "Program stage is specified but does not exist: " + programStageInstanceFilter.getProgramStage() );
            }
        }

        EventQueryCriteria eventQC = programStageInstanceFilter.getEventQueryCriteria();
        if ( eventQC != null )
        {
            if ( eventQC.getOrganisationUnit() != null )
            {
                OrganisationUnit ou = organisationUnitService.getOrganisationUnit( eventQC.getOrganisationUnit() );
                if ( ou == null )
                {
                    errors.add( "Org unit is specified but does not exist: " + eventQC.getOrganisationUnit() );
                }
            }
            if ( eventQC.getAssignedUserMode() != null && eventQC.getAssignedUsers() != null && !eventQC.getAssignedUsers().isEmpty()
                && !eventQC.getAssignedUserMode().equals( AssignedUserSelectionMode.PROVIDED ) )
            {
                errors.add( "Assigned User uid(s) cannot be specified if selectionMode is not PROVIDED" );
            }

            if ( eventQC.getEvents() != null && !eventQC.getEvents().isEmpty() && eventQC.getDataFilters() != null && !eventQC.getDataFilters().isEmpty() )
            {
                errors.add( "Event UIDs and filters can not be specified at the same time" );
            }

            if ( eventQC.getDisplayColumnOrder() != null && eventQC.getDisplayColumnOrder().size() > 0 && (new HashSet<String>( eventQC.getDisplayColumnOrder() )).size() < eventQC.getDisplayColumnOrder().size() )
            {
                errors.add( "Event query criteria can not have duplicate column ordering fields" );
            }
        }

        return errors;
    }

}