package com.mass3d.sms.listener;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import com.mass3d.category.CategoryService;
import com.mass3d.dataelement.DataElement;
import com.mass3d.eventdatavalue.EventDataValue;
import com.mass3d.message.MessageSender;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.program.ProgramInstance;
import com.mass3d.program.ProgramInstanceService;
import com.mass3d.program.ProgramStageInstance;
import com.mass3d.program.ProgramStageInstanceService;
import com.mass3d.program.ProgramStatus;
import com.mass3d.sms.command.SMSCommand;
import com.mass3d.sms.command.code.SMSCode;
import com.mass3d.sms.incoming.IncomingSms;
import com.mass3d.sms.incoming.IncomingSmsService;
import com.mass3d.sms.incoming.SmsMessageStatus;
import com.mass3d.system.util.SmsUtils;
import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import com.mass3d.user.UserService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class CommandSMSListener
    extends
    BaseSMSListener
{
    private static final String DEFAULT_PATTERN = "([^\\s|=]+)\\s*\\=\\s*([^|=]+)\\s*(\\=|$)*\\s*";

    protected static final int INFO = 1;

    protected static final int WARNING = 2;

    protected static final int ERROR = 3;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    protected final ProgramInstanceService programInstanceService;

    protected final CategoryService dataElementCategoryService;

    protected final ProgramStageInstanceService programStageInstanceService;

    protected final UserService userService;

    protected final CurrentUserService currentUserService;

    public CommandSMSListener( ProgramInstanceService programInstanceService,
        CategoryService dataElementCategoryService, ProgramStageInstanceService programStageInstanceService,
        UserService userService, CurrentUserService currentUserService, IncomingSmsService incomingSmsService,
        MessageSender smsSender )
    {
        super( incomingSmsService, smsSender );

        checkNotNull( programInstanceService );
        checkNotNull( dataElementCategoryService );
        checkNotNull( programStageInstanceService );
        checkNotNull( userService );
        checkNotNull( currentUserService );

        this.programInstanceService = programInstanceService;
        this.dataElementCategoryService = dataElementCategoryService;
        this.programStageInstanceService = programStageInstanceService;
        this.userService = userService;
        this.currentUserService = currentUserService;
    }

    @Override
    public boolean accept( IncomingSms sms )
    {
        if ( sms == null )
        {
            return false;
        }

        SMSCommand smsCommand = getSMSCommand( sms );

        return smsCommand != null;
    }

    @Override
    public void receive( IncomingSms sms )
    {
        SMSCommand smsCommand = getSMSCommand( sms );

        Map<String, String> parsedMessage = this.parseMessageInput( sms, smsCommand );

        if ( !hasCorrectFormat( sms, smsCommand ) || !validateInputValues( parsedMessage, smsCommand, sms ) )
        {
            return;
        }

        postProcess( sms, smsCommand, parsedMessage );
    }

    protected abstract void postProcess( IncomingSms sms, SMSCommand smsCommand, Map<String, String> parsedMessage );

    protected abstract SMSCommand getSMSCommand( IncomingSms sms );

    protected boolean hasCorrectFormat( IncomingSms sms, SMSCommand smsCommand )
    {
        String regexp = DEFAULT_PATTERN;

        if ( smsCommand.getSeparator() != null && !smsCommand.getSeparator().trim().isEmpty() )
        {
            regexp = regexp.replaceAll( "=", smsCommand.getSeparator() );
        }

        Pattern pattern = Pattern.compile( regexp );

        Matcher matcher = pattern.matcher( sms.getText() );

        if ( !matcher.find() )
        {
            sendFeedback(
                StringUtils.defaultIfEmpty( smsCommand.getWrongFormatMessage(), SMSCommand.WRONG_FORMAT_MESSAGE ),
                sms.getOriginator(), ERROR );
            return false;
        }

        return true;
    }

    protected Set<OrganisationUnit> getOrganisationUnits( IncomingSms sms )
    {
        User user = getUser( sms );

        if ( user == null )
        {
            return new HashSet<>();
        }

        return SmsUtils.getOrganisationUnitsByPhoneNumber( sms.getOriginator(), Collections.singleton( user ) )
            .get( user.getUid() );
    }

    protected User getUser( IncomingSms sms )
    {
        return userService.getUser( sms.getUser().getUid() );
    }

    protected boolean validateInputValues( Map<String, String> commandValuePairs, SMSCommand smsCommand,
        IncomingSms sms )
    {
        if ( !hasMandatoryParameters( commandValuePairs.keySet(), smsCommand.getCodes() ) )
        {
            sendFeedback( StringUtils.defaultIfEmpty( smsCommand.getDefaultMessage(), SMSCommand.PARAMETER_MISSING ),
                sms.getOriginator(), ERROR );

            return false;
        }

        if ( !hasOrganisationUnit( sms ) )
        {
            sendFeedback( StringUtils.defaultIfEmpty( smsCommand.getNoUserMessage(), SMSCommand.NO_USER_MESSAGE ),
                sms.getOriginator(), ERROR );

            return false;
        }

        if ( hasMultipleOrganisationUnits( sms ) )
        {
            sendFeedback( StringUtils.defaultIfEmpty( smsCommand.getMoreThanOneOrgUnitMessage(),
                SMSCommand.MORE_THAN_ONE_ORGUNIT_MESSAGE ), sms.getOriginator(), ERROR );

            return false;
        }

        return true;
    }

    protected void register( List<ProgramInstance> programInstances, Map<String, String> commandValuePairs,
        SMSCommand smsCommand, IncomingSms sms, Set<OrganisationUnit> ous )
    {
        if ( programInstances.isEmpty() )
        {
            ProgramInstance pi = new ProgramInstance();
            pi.setEnrollmentDate( new Date() );
            pi.setIncidentDate( new Date() );
            pi.setProgram( smsCommand.getProgram() );
            pi.setStatus( ProgramStatus.ACTIVE );

            programInstanceService.addProgramInstance( pi );

            programInstances.add( pi );
        }
        else if ( programInstances.size() > 1 )
        {
            update( sms, SmsMessageStatus.FAILED, false );

            sendFeedback( "Multiple active program instances exists for program: " + smsCommand.getProgram().getUid(),
                sms.getOriginator(), ERROR );

            return;
        }

        ProgramInstance programInstance = programInstances.get( 0 );

        String currentUserName = currentUserService.getCurrentUsername();

        ProgramStageInstance programStageInstance = new ProgramStageInstance();
        programStageInstance.setOrganisationUnit( ous.iterator().next() );
        programStageInstance.setProgramStage( smsCommand.getProgramStage() );
        programStageInstance.setProgramInstance( programInstance );
        programStageInstance.setExecutionDate( sms.getSentDate() );
        programStageInstance.setDueDate( sms.getSentDate() );
        programStageInstance.setAttributeOptionCombo( dataElementCategoryService.getDefaultCategoryOptionCombo() );
        programStageInstance.setCompletedBy( "DHIS 2" );
        programStageInstance.setStoredBy( currentUserName );

        Map<DataElement, EventDataValue> dataElementsAndEventDataValues = new HashMap<>();
        for ( SMSCode smsCode : smsCommand.getCodes() )
        {
            EventDataValue eventDataValue = new EventDataValue( smsCode.getDataElement().getUid(),
                commandValuePairs.get( smsCode.getCode() ), currentUserName );
            eventDataValue.setAutoFields();

            // Filter empty values out -> this is "adding/saving/creating",
            // therefore, empty values are ignored
            if ( !StringUtils.isEmpty( eventDataValue.getValue() ) )
            {
                dataElementsAndEventDataValues.put( smsCode.getDataElement(), eventDataValue );
            }
        }

        programStageInstanceService.saveEventDataValuesAndSaveProgramStageInstance( programStageInstance,
            dataElementsAndEventDataValues );

        update( sms, SmsMessageStatus.PROCESSED, true );

        sendFeedback( StringUtils.defaultIfEmpty( smsCommand.getSuccessMessage(), SMSCommand.SUCCESS_MESSAGE ),
            sms.getOriginator(), INFO );
    }

    protected Map<String, String> parseMessageInput( IncomingSms sms, SMSCommand smsCommand )
    {
        HashMap<String, String> output = new HashMap<>();

        Pattern pattern = Pattern.compile( DEFAULT_PATTERN );

        if ( !StringUtils.isBlank( smsCommand.getSeparator() ) )
        {
            String regex = DEFAULT_PATTERN.replaceAll( "=", smsCommand.getSeparator() );

            pattern = Pattern.compile( regex );
        }

        Matcher matcher = pattern.matcher( sms.getText() );
        while ( matcher.find() )
        {
            String key = matcher.group( 1 ).trim();
            String value = matcher.group( 2 ).trim();

            if ( !StringUtils.isEmpty( key ) && !StringUtils.isEmpty( value ) )
            {
                output.put( key, value );
            }
        }

        return output;
    }

    // -------------------------------------------------------------------------
    // Supportive Methods
    // -------------------------------------------------------------------------

    private boolean hasMandatoryParameters( Set<String> keySet, Set<SMSCode> smsCodes )
    {
        for ( SMSCode smsCode : smsCodes )
        {
            if ( smsCode.isCompulsory() && !keySet.contains( smsCode.getCode() ) )
            {
                return false;
            }
        }

        return true;
    }

    private boolean hasOrganisationUnit( IncomingSms sms )
    {
        Collection<OrganisationUnit> orgUnits = getOrganisationUnits( sms );

        return !(orgUnits == null || orgUnits.isEmpty());

    }

    private boolean hasMultipleOrganisationUnits( IncomingSms sms )
    {
        List<User> users = userService.getUsersByPhoneNumber( sms.getOriginator() );

        Set<OrganisationUnit> organisationUnits = users.stream().flatMap( user -> user.getOrganisationUnits().stream() )
            .collect( Collectors.toSet() );

        return organisationUnits.size() > 1;
    }
}
