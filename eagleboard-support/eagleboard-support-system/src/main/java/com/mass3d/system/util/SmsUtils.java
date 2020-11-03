package com.mass3d.system.util;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import com.mass3d.commons.util.TextUtils;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.sms.command.SMSCommand;
import com.mass3d.sms.incoming.IncomingSms;
import com.mass3d.sms.parse.SMSParserException;
import com.mass3d.user.User;

public class SmsUtils
{
    private static final int MAX_CHAR = 160;

    private static final String COMMAND_PATTERN = "([A-Za-z])\\w+";

    public static String getCommandString( IncomingSms sms )
    {
        return getCommandString( sms.getText() );
    }

    public static String getCommandString( String text )
    {
        String commandString = null;

        Pattern pattern = Pattern.compile( COMMAND_PATTERN );

        Matcher matcher = pattern.matcher( text );

        if ( matcher.find() )
        {
            commandString = matcher.group();
            commandString = commandString.trim();
        }

        return commandString;
    }

    public static boolean isBase64( IncomingSms sms )
    {
        try
        {
            Base64.getDecoder().decode( sms.getText() );
            return true;
        }
        catch ( IllegalArgumentException e )
        {
            return false;
        }
    }

    public static byte[] getBytes( IncomingSms sms )
    {
        try
        {
            byte[] bytes = Base64.getDecoder().decode( sms.getText() );
            return bytes;
        }
        catch ( IllegalArgumentException e )
        {
            return null;
        }
    }

    public static Map<String, Set<OrganisationUnit>> getOrganisationUnitsByPhoneNumber( String sender,
        Collection<User> users )
    {
        Map<String, Set<OrganisationUnit>> userOrgUnitMap = new HashMap<>();

        for ( User u : users )
        {
            if ( u.getOrganisationUnits() != null )
            {
                userOrgUnitMap.put( u.getUid(), u.getOrganisationUnits() );
            }
        }

        return userOrgUnitMap;
    }

    public static Date lookForDate( String message )
    {
        if ( !message.contains( " " ) )
        {
            return null;
        }

        Date date = null;
        String[] messageSplit = message.trim().split( " " );
        // The first element in the split is the sms command. If there are only
        // two elements
        // in the split assume the 2nd is data values, not date.
        if ( messageSplit.length <= 2 )
        {
            return null;
        }
        String dateString = messageSplit[1];
        SimpleDateFormat format = new SimpleDateFormat( "ddMM" );

        try
        {
            Calendar cal = Calendar.getInstance();
            date = format.parse( dateString );
            cal.setTime( date );
            int year = Calendar.getInstance().get( Calendar.YEAR );
            int month = Calendar.getInstance().get( Calendar.MONTH );

            if ( cal.get( Calendar.MONTH ) <= month )
            {
                cal.set( Calendar.YEAR, year );
            }
            else
            {
                cal.set( Calendar.YEAR, year - 1 );
            }

            date = cal.getTime();
        }
        catch ( Exception e )
        {
            // no date found
        }

        return date;
    }

    public static User getUser( String sender, SMSCommand smsCommand, List<User> userList )
    {
        OrganisationUnit orgunit = null;
        User user = null;

        for ( User u : userList )
        {
            OrganisationUnit ou = u.getOrganisationUnit();

            if ( ou != null )
            {
                if ( orgunit == null )
                {
                    orgunit = ou;
                }
                else if ( orgunit.getId() == ou.getId() )
                {
                }
                else
                {
                    throw new SMSParserException( StringUtils.defaultIfBlank( smsCommand.getMoreThanOneOrgUnitMessage(),
                        SMSCommand.MORE_THAN_ONE_ORGUNIT_MESSAGE ) );
                }
            }

            user = u;
        }

        if ( user == null )
        {
            throw new SMSParserException( "User is not associated with any orgunit. Please contact your supervisor." );
        }

        return user;
    }

    public static List<String> splitLongUnicodeString( String message, List<String> result )
    {
        String firstTempString = null;
        String secondTempString = null;
        int indexToCut = 0;

        firstTempString = message.substring( 0, MAX_CHAR );

        indexToCut = firstTempString.lastIndexOf( " " );

        firstTempString = firstTempString.substring( 0, indexToCut );

        result.add( firstTempString );

        secondTempString = message.substring( indexToCut + 1, message.length() );

        if ( secondTempString.length() <= MAX_CHAR )
        {
            result.add( secondTempString );
            return result;
        }
        else
        {
            return splitLongUnicodeString( secondTempString, result );
        }
    }

    public static Set<String> getRecipientsPhoneNumber( Collection<User> users )
    {
        return users.parallelStream().filter( u -> u.getPhoneNumber() != null && !u.getPhoneNumber().isEmpty() )
            .map( u -> u.getPhoneNumber() ).collect( Collectors.toSet() );
    }

    public static Set<String> getRecipientsEmail( Collection<User> users )
    {
        Set<String> recipients = new HashSet<>();

        for ( User user : users )
        {
            String email = user.getEmail();

            if ( StringUtils.trimToNull( email ) != null )
            {
                recipients.add( email );
            }
        }
        return recipients;
    }

    public static OrganisationUnit selectOrganisationUnit( Collection<OrganisationUnit> orgUnits,
        Map<String, String> parsedMessage, SMSCommand smsCommand )
    {
        OrganisationUnit orgUnit = null;

        for ( OrganisationUnit o : orgUnits )
        {
            if ( orgUnits.size() == 1 )
            {
                orgUnit = o;
            }
            if ( parsedMessage.containsKey( "ORG" ) && o.getCode().equals( parsedMessage.get( "ORG" ) ) )
            {
                orgUnit = o;
                break;
            }
        }

        if ( orgUnit == null && orgUnits.size() > 1 )
        {
            String messageListingOrgUnits = smsCommand.getMoreThanOneOrgUnitMessage();

            for ( Iterator<OrganisationUnit> i = orgUnits.iterator(); i.hasNext(); )
            {
                OrganisationUnit o = i.next();
                messageListingOrgUnits += TextUtils.SPACE + o.getName() + ":" + o.getCode();

                if ( i.hasNext() )
                {
                    messageListingOrgUnits += ",";
                }
            }

            throw new SMSParserException( messageListingOrgUnits );
        }

        return orgUnit;
    }

    public static String removePhoneNumberPrefix( String number )
    {
        if ( number == null )
        {
            return null;
        }

        if ( number.startsWith( "00" ) )
        {
            number = number.substring( 2, number.length() );
        }
        else if ( number.startsWith( "+" ) )
        {
            number = number.substring( 1, number.length() );
        }

        return number;
    }
}
