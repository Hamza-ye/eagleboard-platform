package com.mass3d.commons.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mass3d.organisationunit.OrganisationUnitGroupService;
import com.mass3d.organisationunit.OrganisationUnitGroupSet;
import com.mass3d.paging.ActionPagingSupport;
import com.mass3d.common.IdentifiableObjectUtils;

public class GetOrganisationUnitGroupSetsAction
    extends ActionPagingSupport<OrganisationUnitGroupSet>
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
    // Input & Output
    // -------------------------------------------------------------------------

    private String key;

    public void setKey( String key )
    {
        this.key = key;
    }

    private List<OrganisationUnitGroupSet> organisationUnitGroupSets;

    public List<OrganisationUnitGroupSet> getOrganisationUnitGroupSets()
    {
        return organisationUnitGroupSets;
    }

    // -------------------------------------------------------------------------
    // Action Implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        organisationUnitGroupSets = new ArrayList<>(
            organisationUnitGroupService.getAllOrganisationUnitGroupSets() );

        if ( key != null )
        {
            organisationUnitGroupSets = IdentifiableObjectUtils.filterNameByKey( organisationUnitGroupSets, key, true );
        }

        Collections.sort( organisationUnitGroupSets );

        if ( usePaging )
        {
            this.paging = createPaging( organisationUnitGroupSets.size() );

            organisationUnitGroupSets = organisationUnitGroupSets.subList( paging.getStartPos(), paging.getEndPos() );
        }

        return SUCCESS;
    }

}
