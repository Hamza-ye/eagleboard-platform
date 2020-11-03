package com.mass3d.webapi.controller;

import org.apache.commons.lang.StringUtils;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DhisApiVersion;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.configuration.Configuration;
import com.mass3d.configuration.ConfigurationService;
import com.mass3d.dataelement.DataElementGroup;
import com.mass3d.external.conf.DhisConfigurationProvider;
import com.mass3d.indicator.IndicatorGroup;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.organisationunit.OrganisationUnitLevel;
import com.mass3d.period.PeriodService;
import com.mass3d.period.PeriodType;
import com.mass3d.render.RenderService;
import com.mass3d.setting.SettingKey;
import com.mass3d.setting.SystemSettingManager;
import com.mass3d.user.UserAuthorityGroup;
import com.mass3d.user.UserGroup;
import com.mass3d.util.ObjectUtils;
import com.mass3d.webapi.controller.exception.NotFoundException;
import com.mass3d.webapi.mvc.annotation.ApiVersion;
import com.mass3d.appmanager.AppManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping( "/configuration" )
@ApiVersion( { DhisApiVersion.DEFAULT, DhisApiVersion.ALL } )
public class ConfigurationController
{
    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private DhisConfigurationProvider config;

    @Autowired
    private IdentifiableObjectManager identifiableObjectManager;

    @Autowired
    private PeriodService periodService;

    @Autowired
    private RenderService renderService;

    @Autowired
    private SystemSettingManager systemSettingManager;

    @Autowired
    private AppManager appManager;

    // -------------------------------------------------------------------------
    // Resources
    // -------------------------------------------------------------------------

    @RequestMapping( method = RequestMethod.GET )
    public @ResponseBody Configuration getConfiguration( Model model, HttpServletRequest request )
    {
        return configurationService.getConfiguration();
    }

    @ResponseStatus( value = HttpStatus.OK )
    @RequestMapping( value = "/systemId", method = RequestMethod.GET )
    public @ResponseBody String getSystemId( Model model, HttpServletRequest request )
    {
        return configurationService.getConfiguration().getSystemId();
    }

    @PreAuthorize( "hasRole('ALL')" )
    @RequestMapping( value = "/systemId", method = RequestMethod.POST )
    @ResponseStatus( value = HttpStatus.NO_CONTENT )
    public void setSystemId( @RequestBody( required = false ) String systemId )
    {
        systemId = ObjectUtils.firstNonNull( systemId, UUID.randomUUID().toString() );

        Configuration config = configurationService.getConfiguration();
        config.setSystemId( systemId );
        configurationService.setConfiguration( config );
    }

    @RequestMapping( value = "/feedbackRecipients", method = RequestMethod.GET )
    public @ResponseBody UserGroup getFeedbackRecipients( Model model, HttpServletRequest request )
    {
        return configurationService.getConfiguration().getFeedbackRecipients();
    }

    @PreAuthorize( "hasRole('ALL') or hasRole('F_SYSTEM_SETTING')" )
    @RequestMapping( value = "/feedbackRecipients", method = RequestMethod.POST )
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public void setFeedbackRecipients( @RequestBody String uid )
        throws NotFoundException
    {
        uid = StringUtils.remove( uid, "\"" );
        UserGroup group = identifiableObjectManager.get( UserGroup.class, uid );

        if ( group == null )
        {
            throw new NotFoundException( "User group", uid );
        }

        Configuration config = configurationService.getConfiguration();

        config.setFeedbackRecipients( group );

        configurationService.setConfiguration( config );
    }

    @PreAuthorize( "hasRole('ALL') or hasRole('F_SYSTEM_SETTING')" )
    @RequestMapping( value = "/feedbackRecipients", method = RequestMethod.DELETE )
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public void removeFeedbackRecipients()
    {
        Configuration config = configurationService.getConfiguration();

        config.setFeedbackRecipients( null );

        configurationService.setConfiguration( config );
    }

    @RequestMapping( value = "/offlineOrganisationUnitLevel", method = RequestMethod.GET )
    public @ResponseBody OrganisationUnitLevel getOfflineOrganisationUnitLevel( Model model, HttpServletRequest request )
    {
        return configurationService.getConfiguration().getOfflineOrganisationUnitLevel();
    }

    @PreAuthorize( "hasRole('ALL') or hasRole('F_SYSTEM_SETTING')" )
    @RequestMapping( value = "/offlineOrganisationUnitLevel", method = RequestMethod.POST )
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public void setOfflineOrganisationUnitLevel( @RequestBody String uid )
        throws NotFoundException
    {
        uid = StringUtils.remove( uid, "\"" );
        OrganisationUnitLevel organisationUnitLevel = identifiableObjectManager.get( OrganisationUnitLevel.class, uid );

        if ( organisationUnitLevel == null )
        {
            throw new NotFoundException( "Organisation unit level", uid );
        }

        Configuration config = configurationService.getConfiguration();

        config.setOfflineOrganisationUnitLevel( organisationUnitLevel );

        configurationService.setConfiguration( config );
    }

    @PreAuthorize( "hasRole('ALL') or hasRole('F_SYSTEM_SETTING')" )
    @RequestMapping( value = "/offlineOrganisationUnitLevel", method = RequestMethod.DELETE )
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public void removeOfflineOrganisationUnitLevel()
    {
        Configuration config = configurationService.getConfiguration();

        config.setOfflineOrganisationUnitLevel( null );

        configurationService.setConfiguration( config );
    }

    @RequestMapping( value = "/infrastructuralIndicators", method = RequestMethod.GET )
    public @ResponseBody IndicatorGroup getInfrastructuralIndicators( Model model, HttpServletRequest request )
    {
        return configurationService.getConfiguration().getInfrastructuralIndicators();
    }

    @PreAuthorize( "hasRole('ALL') or hasRole('F_SYSTEM_SETTING')" )
    @RequestMapping( value = "/infrastructuralIndicators", method = RequestMethod.POST )
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public void setInfrastructuralIndicators( @RequestBody String uid )
        throws NotFoundException
    {
        uid = StringUtils.remove( uid, "\"" );
        IndicatorGroup group = identifiableObjectManager.get( IndicatorGroup.class, uid );

        if ( group == null )
        {
            throw new NotFoundException( "Indicator group", uid );
        }

        Configuration config = configurationService.getConfiguration();

        config.setInfrastructuralIndicators( group );

        configurationService.setConfiguration( config );
    }

    @RequestMapping( value = "/infrastructuralDataElements", method = RequestMethod.GET )
    public @ResponseBody DataElementGroup getInfrastructuralDataElements( Model model, HttpServletRequest request )
    {
        return configurationService.getConfiguration().getInfrastructuralDataElements();
    }

    @PreAuthorize( "hasRole('ALL') or hasRole('F_SYSTEM_SETTING')" )
    @RequestMapping( value = "/infrastructuralDataElements", method = RequestMethod.POST )
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public void setInfrastructuralDataElements( @RequestBody String uid )
        throws NotFoundException
    {
        uid = StringUtils.remove( uid, "\"" );
        DataElementGroup group = identifiableObjectManager.get( DataElementGroup.class, uid );

        if ( group == null )
        {
            throw new NotFoundException( "Data element group", uid );
        }

        Configuration config = configurationService.getConfiguration();

        config.setInfrastructuralDataElements( group );

        configurationService.setConfiguration( config );
    }

    @RequestMapping( value = "/infrastructuralPeriodType", method = RequestMethod.GET )
    public @ResponseBody BaseIdentifiableObject getInfrastructuralPeriodType( Model model, HttpServletRequest request )
    {
        String name = configurationService.getConfiguration().getInfrastructuralPeriodTypeDefaultIfNull().getName();
        return new BaseIdentifiableObject( name, name, name );
    }

    @PreAuthorize( "hasRole('ALL') or hasRole('F_SYSTEM_SETTING')" )
    @RequestMapping( value = "/infrastructuralPeriodType", method = RequestMethod.POST )
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public void setInfrastructuralPeriodType( @RequestBody String name )
        throws NotFoundException
    {
        name = StringUtils.remove( name, "\"" );
        PeriodType periodType = PeriodType.getPeriodTypeByName( name );

        if ( periodType == null )
        {
            throw new NotFoundException( "Period type", name );
        }

        Configuration config = configurationService.getConfiguration();

        periodType = periodService.reloadPeriodType( periodType );

        config.setInfrastructuralPeriodType( periodType );

        configurationService.setConfiguration( config );
    }

    @RequestMapping( value = "/selfRegistrationRole", method = RequestMethod.GET )
    public @ResponseBody UserAuthorityGroup getSelfRegistrationRole( Model model, HttpServletRequest request )
    {
        return configurationService.getConfiguration().getSelfRegistrationRole();
    }

    @PreAuthorize( "hasRole('ALL') or hasRole('F_SYSTEM_SETTING')" )
    @RequestMapping( value = "/selfRegistrationRole", method = RequestMethod.POST )
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public void setSelfRegistrationRole( @RequestBody String uid )
        throws NotFoundException
    {
        uid = StringUtils.remove( uid, "\"" );
        UserAuthorityGroup userGroup = identifiableObjectManager.get( UserAuthorityGroup.class, uid );

        if ( userGroup == null )
        {
            throw new NotFoundException( "User authority group", uid );
        }

        Configuration config = configurationService.getConfiguration();

        config.setSelfRegistrationRole( userGroup );

        configurationService.setConfiguration( config );
    }

    @PreAuthorize( "hasRole('ALL') or hasRole('F_SYSTEM_SETTING')" )
    @RequestMapping( value = "/selfRegistrationRole", method = RequestMethod.DELETE )
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public void removeSelfRegistrationRole()
    {
        Configuration config = configurationService.getConfiguration();

        config.setSelfRegistrationRole( null );

        configurationService.setConfiguration( config );
    }

    @RequestMapping( value = "/selfRegistrationOrgUnit", method = RequestMethod.GET )
    public @ResponseBody OrganisationUnit getSelfRegistrationOrgUnit( Model model, HttpServletRequest request )
    {
        return configurationService.getConfiguration().getSelfRegistrationOrgUnit();
    }

    @PreAuthorize( "hasRole('ALL') or hasRole('F_SYSTEM_SETTING')" )
    @RequestMapping( value = "/selfRegistrationOrgUnit", method = RequestMethod.POST )
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public void setSelfRegistrationOrgUnit( @RequestBody String uid )
        throws NotFoundException
    {
        uid = StringUtils.remove( uid, "\"" );
        OrganisationUnit orgunit = identifiableObjectManager.get( OrganisationUnit.class, uid );

        if ( orgunit == null )
        {
            throw new NotFoundException( "Organisation unit", uid );
        }

        Configuration config = configurationService.getConfiguration();

        config.setSelfRegistrationOrgUnit( orgunit );

        configurationService.setConfiguration( config );
    }

    @PreAuthorize( "hasRole('ALL') or hasRole('F_SYSTEM_SETTING')" )
    @RequestMapping( value = "/selfRegistrationOrgUnit", method = RequestMethod.DELETE )
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public void removeSelfRegistrationOrgUnit()
    {
        Configuration config = configurationService.getConfiguration();

        config.setSelfRegistrationOrgUnit( null );

        configurationService.setConfiguration( config );
    }

    @RequestMapping( value = "/remoteServerUrl", method = RequestMethod.GET )
    public @ResponseBody String getRemoteServerUrl( Model model, HttpServletRequest request )
    {
        return (String) systemSettingManager.getSystemSetting( SettingKey.REMOTE_INSTANCE_URL );
    }

    @RequestMapping( value = "/remoteServerUsername", method = RequestMethod.GET )
    public @ResponseBody String getRemoteServerUsername( Model model, HttpServletRequest request )
    {
        return (String) systemSettingManager.getSystemSetting( SettingKey.REMOTE_INSTANCE_USERNAME );
    }

    @RequestMapping( value = "/corsWhitelist", method = RequestMethod.GET, produces = "application/json" )
    public @ResponseBody Set<String> getCorsWhitelist( Model model, HttpServletRequest request )
    {
        return configurationService.getConfiguration().getCorsWhitelist();
    }

    @SuppressWarnings( "unchecked" )
    @PreAuthorize( "hasRole('ALL') or hasRole('F_SYSTEM_SETTING')" )
    @RequestMapping( value = "/corsWhitelist", method = RequestMethod.POST, consumes = "application/json" )
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public void setCorsWhitelist( @RequestBody String input )
        throws IOException
    {
        Set<String> corsWhitelist = renderService.fromJson( input, Set.class );

        Configuration config = configurationService.getConfiguration();

        config.setCorsWhitelist( corsWhitelist );

        configurationService.setConfiguration( config );
    }

    @RequestMapping( value = "/systemReadOnlyMode", method = RequestMethod.GET )
    public @ResponseBody boolean getSystemReadOnlyMode( Model model, HttpServletRequest request )
    {
        return config.isReadOnlyMode();
    }

    @RequestMapping( value = "/appHubUrl", method = RequestMethod.GET )
    public @ResponseBody String getAppHubUrl( Model model, HttpServletRequest request )
    {
        return appManager.getAppHubUrl();
    }
}
