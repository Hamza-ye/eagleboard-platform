package com.mass3d.commons.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mass3d.organisationunit.OrganisationUnitGroup;
import com.mass3d.organisationunit.OrganisationUnitGroupService;
import com.mass3d.paging.ActionPagingSupport;
import com.mass3d.common.IdentifiableObjectUtils;

public class GetOrganisationUnitGroupsAction
    extends ActionPagingSupport<OrganisationUnitGroup>
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
    // Input & output
    // -------------------------------------------------------------------------

    private String key;

    public void setKey( String key )
    {
        this.key = key;
    }

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
        organisationUnitGroups = new ArrayList<>(
            organisationUnitGroupService.getAllOrganisationUnitGroups() );

        if ( key != null )
        {
            organisationUnitGroups = IdentifiableObjectUtils.filterNameByKey( organisationUnitGroups, key, true );
        }

        Collections.sort( organisationUnitGroups );

        if ( usePaging )
        {
            this.paging = createPaging( organisationUnitGroups.size() );

            organisationUnitGroups = organisationUnitGroups.subList( paging.getStartPos(), paging.getEndPos() );
        }

        return SUCCESS;
    }
}
