package com.mass3d.commons.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.organisationunit.OrganisationUnitService;
import com.mass3d.paging.ActionPagingSupport;

public class GetOrganisationUnitChildrenAction
    extends ActionPagingSupport<OrganisationUnit>
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer organisationUnitId )
    {
        this.id = organisationUnitId;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<OrganisationUnit> organisationUnits;

    public List<OrganisationUnit> getOrganisationUnits()
    {
        return organisationUnits;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        OrganisationUnit unit = organisationUnitService.getOrganisationUnit( id );

        organisationUnits = new ArrayList<>( unit.getChildren() );

        Collections.sort( organisationUnits );

        if ( usePaging )
        {
            this.paging = createPaging( organisationUnits.size() );

            organisationUnits = organisationUnits.subList( paging.getStartPos(), paging.getEndPos() );
        }

        return SUCCESS;
    }
}
