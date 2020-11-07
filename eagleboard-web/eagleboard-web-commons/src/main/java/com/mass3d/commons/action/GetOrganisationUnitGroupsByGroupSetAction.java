package com.mass3d.commons.action;

import java.util.ArrayList;
import java.util.List;

import com.mass3d.organisationunit.OrganisationUnitGroup;
import com.mass3d.organisationunit.OrganisationUnitGroupService;

import com.opensymphony.xwork2.Action;

public class GetOrganisationUnitGroupsByGroupSetAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitGroupService organisationUnitGroupService;

    public void setOrganisationUnitGroupService( OrganisationUnitGroupService organisationUnitGroupService )
    {
        this.organisationUnitGroupService = organisationUnitGroupService;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<OrganisationUnitGroup> organisationUnitGroups;

    public List<OrganisationUnitGroup> getOrganisationUnitGroups()
    {
        return organisationUnitGroups;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        if ( id != null )
        {
            organisationUnitGroups = new ArrayList<>( organisationUnitGroupService
                .getOrganisationUnitGroupSet( id ).getOrganisationUnitGroups() );
        }

        return SUCCESS;
    }
}
