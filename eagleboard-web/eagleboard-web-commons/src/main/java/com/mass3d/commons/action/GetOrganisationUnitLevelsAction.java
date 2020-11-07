package com.mass3d.commons.action;

import java.util.ArrayList;
import java.util.List;

import com.mass3d.organisationunit.OrganisationUnitLevel;
import com.mass3d.organisationunit.OrganisationUnitService;
import com.mass3d.paging.ActionPagingSupport;

public class GetOrganisationUnitLevelsAction
    extends ActionPagingSupport<OrganisationUnitLevel>
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
    // Output
    // -------------------------------------------------------------------------

    private List<OrganisationUnitLevel> organisationUnitLevels;

    public List<OrganisationUnitLevel> getOrganisationUnitLevels()
    {
        return organisationUnitLevels;
    }

    // -------------------------------------------------------------------------
    // Action Implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        organisationUnitLevels = new ArrayList<>(
            organisationUnitService.getOrganisationUnitLevels() );

        if ( usePaging )
        {
            this.paging = createPaging( organisationUnitLevels.size() );

            organisationUnitLevels = organisationUnitLevels.subList( paging.getStartPos(), paging.getEndPos() );
        }

        return SUCCESS;
    }
}
