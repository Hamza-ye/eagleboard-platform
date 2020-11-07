package com.mass3d.commons.action;

import java.util.*;

import com.mass3d.common.SortProperty;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.organisationunit.OrganisationUnitLevel;
import com.mass3d.organisationunit.OrganisationUnitService;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import com.mass3d.user.UserSettingKey;
import com.mass3d.user.UserSettingService;
import com.mass3d.version.Version;
import com.mass3d.version.VersionService;

import com.opensymphony.xwork2.Action;

public class GetOrganisationUnitTreeAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    private VersionService versionService;

    public void setVersionService( VersionService versionService )
    {
        this.versionService = versionService;
    }

    private UserSettingService userSettingService;

    public void setUserSettingService( UserSettingService userSettingService )
    {
        this.userSettingService = userSettingService;
    }

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private List<OrganisationUnit> organisationUnits = new ArrayList<>();

    public List<OrganisationUnit> getOrganisationUnits()
    {
        return organisationUnits;
    }

    private List<OrganisationUnit> rootOrganisationUnits = new ArrayList<>();

    public List<OrganisationUnit> getRootOrganisationUnits()
    {
        return rootOrganisationUnits;
    }

    private String version;

    public String getVersion()
    {
        return version;
    }

    private String username;

    public String getUsername()
    {
        return username;
    }

    private SortProperty sortBy;

    public SortProperty getSortBy()
    {
        return sortBy;
    }

    private boolean versionOnly;

    public void setVersionOnly( Boolean versionOnly )
    {
        this.versionOnly = versionOnly;
    }

    public Boolean getVersionOnly()
    {
        return versionOnly;
    }

    private String parentId;

    public void setParentId( String parentId )
    {
        this.parentId = parentId;
    }

    private String leafId;

    public void setLeafId( String leafId )
    {
        this.leafId = leafId;
    }

    private String byName;

    public void setByName( String byName )
    {
        this.byName = byName;
    }

    private boolean realRoot;

    public boolean isRealRoot()
    {
        return realRoot;
    }

    private Integer offlineLevel;

    public void setOfflineLevel( Integer offlineLevel )
    {
        this.offlineLevel = offlineLevel;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        version = getVersionString();

        username = currentUserService.getCurrentUsername();

        sortBy = SortProperty.fromValue( userSettingService.getUserSetting( UserSettingKey.ANALYSIS_DISPLAY_PROPERTY ).toString() );

        User user = currentUserService.getCurrentUser();

        if ( user != null && user.hasOrganisationUnit() )
        {
            rootOrganisationUnits = new ArrayList<>( user.getOrganisationUnits() );
        }
        else if ( currentUserService.currentUserIsSuper() || user == null )
        {
            rootOrganisationUnits = new ArrayList<>( organisationUnitService.getRootOrganisationUnits() );
        }

        if ( byName != null )
        {
            List<OrganisationUnit> organisationUnitByName = organisationUnitService.getOrganisationUnitByName( byName );

            if ( !organisationUnitByName.isEmpty() )
            {
                OrganisationUnit child = organisationUnitByName.get( 0 );
                organisationUnits.add( child );
                OrganisationUnit parent = child.getParent();

                if ( parent != null )
                {
                    do
                    {
                        organisationUnits.add( parent );
                        organisationUnits.addAll( parent.getChildren() );
                    }
                    while ( (parent = parent.getParent()) != null );
                }

                return "partial";
            }
        }

        if ( leafId != null )
        {
            OrganisationUnit leaf = organisationUnitService.getOrganisationUnit( leafId );

            if ( leaf != null )
            {
                organisationUnits.add( leaf );
                organisationUnits.addAll( leaf.getChildren() );

                for ( OrganisationUnit organisationUnit : leaf.getAncestors() )
                {
                    organisationUnits.add( organisationUnit );
                    organisationUnits.addAll( organisationUnit.getChildren() );
                }
            }

            return "partial";
        }

        if ( parentId != null )
        {
            OrganisationUnit parent = organisationUnitService.getOrganisationUnit( parentId );

            if ( parent != null )
            {
                organisationUnits.addAll( parent.getChildren() );
            }

            return "partial";
        }

        if ( !versionOnly && !rootOrganisationUnits.isEmpty() )
        {
            Integer offlineLevels = getOfflineOrganisationUnitLevels();

            for ( OrganisationUnit unit : rootOrganisationUnits )
            {
                organisationUnits
                    .addAll( organisationUnitService.getOrganisationUnitWithChildren( unit.getId(), offlineLevels ) );
            }
        }

        Collection<?> intersection = org.apache.commons.collections.CollectionUtils.intersection(
            organisationUnitService.getRootOrganisationUnits(), rootOrganisationUnits );

        if ( intersection.size() > 0 )
        {
            realRoot = true;
        }

        Collections.sort( rootOrganisationUnits );

        return SUCCESS;
    }

    private String getVersionString()
    {
        Version orgUnitVersion = versionService.getVersionByKey( VersionService.ORGANISATIONUNIT_VERSION );

        if ( orgUnitVersion == null )
        {
            String uuid = UUID.randomUUID().toString();
            orgUnitVersion = new Version();
            orgUnitVersion.setKey( VersionService.ORGANISATIONUNIT_VERSION );
            orgUnitVersion.setValue( uuid );
            versionService.addVersion( orgUnitVersion );
        }

        return orgUnitVersion.getValue();
    }

    /**
     * Returns the number of org unit levels to cache offline based on the given
     * org unit level argument, next the org unit level from the user org unit,
     * next the level from the configuration.
     */
    private Integer getOfflineOrganisationUnitLevels()
    {
        List<OrganisationUnitLevel> orgUnitLevels = organisationUnitService.getOrganisationUnitLevels();

        if ( orgUnitLevels == null || orgUnitLevels.isEmpty() )
        {
            return null;
        }

        Integer level =
            offlineLevel != null ? offlineLevel : organisationUnitService.getOfflineOrganisationUnitLevels();

        return level == 1 ? 2 : level;
    }
}
