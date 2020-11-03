package com.mass3d.program;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataentryform.DataEntryForm;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.organisationunit.OrganisationUnitQueryParams;
import com.mass3d.organisationunit.OrganisationUnitService;
import com.mass3d.trackedentity.TrackedEntityType;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.program.ProgramService" )
public class DefaultProgramService
    implements ProgramService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final ProgramStore programStore;

    private final CurrentUserService currentUserService;

    private final OrganisationUnitService organisationUnitService;

    public DefaultProgramService( ProgramStore programStore, CurrentUserService currentUserService,
        OrganisationUnitService organisationUnitService )
    {
        checkNotNull( programStore );
        checkNotNull( currentUserService );
        checkNotNull( organisationUnitService );

        this.programStore = programStore;
        this.currentUserService = currentUserService;
        this.organisationUnitService = organisationUnitService;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public long addProgram( Program program )
    {
        programStore.save( program );
        return program.getId();
    }

    @Override
    @Transactional
    public void updateProgram( Program program )
    {
        programStore.update( program );
    }

    @Override
    @Transactional
    public void deleteProgram( Program program )
    {
        programStore.delete( program );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Program> getAllPrograms()
    {
        return programStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Program getProgram( long id )
    {
        return programStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Program> getPrograms( OrganisationUnit organisationUnit )
    {
        return programStore.get( organisationUnit );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Program> getPrograms( ProgramType type )
    {
        return programStore.getByType( type );
    }

    @Override
    @Transactional(readOnly = true)
    public Program getProgram( String uid )
    {
        return programStore.getByUid( uid );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Program> getProgramsByTrackedEntityType( TrackedEntityType trackedEntityType )
    {
        return programStore.getByTrackedEntityType( trackedEntityType );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Program> getProgramsByDataEntryForm( DataEntryForm dataEntryForm )
    {
        return programStore.getByDataEntryForm( dataEntryForm );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Program> getUserPrograms()
    {
        return getUserPrograms( currentUserService.getCurrentUser() );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Program> getUserPrograms( User user )
    {
        if ( user == null || user.isSuper() )
        {
            return getAllPrograms();
        }

        return programStore.getDataReadAll( user );
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Program> getUserPrograms( ProgramType programType )
    {
        return getUserPrograms().stream().filter( p -> p.getProgramType() == programType ).collect( Collectors
            .toSet() );
    }

    @Override
    @Transactional
    public void mergeWithCurrentUserOrganisationUnits( Program program, Collection<OrganisationUnit> mergeOrganisationUnits )
    {
        Set<OrganisationUnit> selectedOrgUnits = Sets.newHashSet( program.getOrganisationUnits() );

        OrganisationUnitQueryParams params = new OrganisationUnitQueryParams();
        params.setParents( currentUserService.getCurrentUser().getOrganisationUnits() );

        Set<OrganisationUnit> userOrganisationUnits = Sets.newHashSet( organisationUnitService.getOrganisationUnitsByQuery( params ) );

        selectedOrgUnits.removeAll( userOrganisationUnits );
        selectedOrgUnits.addAll( mergeOrganisationUnits );

        program.updateOrganisationUnits( selectedOrgUnits );

        updateProgram( program );
    }

    // -------------------------------------------------------------------------
    // ProgramDataElement
    // -------------------------------------------------------------------------


    @Override
    @Transactional(readOnly = true)
    public List<ProgramDataElementDimensionItem> getGeneratedProgramDataElements( String programUid )
    {
        Program program = getProgram( programUid );

        List<ProgramDataElementDimensionItem> programDataElements = Lists.newArrayList();

        if ( program == null )
        {
            return programDataElements;
        }

        for ( DataElement element : program.getDataElements() )
        {
            programDataElements.add( new ProgramDataElementDimensionItem( program, element ) );
        }

        Collections.sort( programDataElements );

        return programDataElements;
    }
}
