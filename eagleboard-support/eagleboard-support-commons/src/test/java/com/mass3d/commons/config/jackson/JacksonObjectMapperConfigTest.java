package com.mass3d.commons.config.jackson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.Map;
import com.mass3d.commons.config.JacksonObjectMapperConfig;
import com.mass3d.user.User;
import com.mass3d.util.DateUtils;
import org.junit.Test;

public class JacksonObjectMapperConfigTest
{
    private ObjectMapper jsonMapper = JacksonObjectMapperConfig.staticJsonMapper();

    @Test
    public void testIsoDateSupport()
        throws JsonProcessingException
    {
        Map<String, Date> yearTest = jsonMapper.readValue( createDateTest( "2019" ), new DateMapTypeReference() );
        assertEquals( yearTest.get( "date" ), DateUtils.parseDate( "2019" ) );
        Map<String, Date> yearMonthTest = jsonMapper.readValue( createDateTest( "2019-01" ),
            new DateMapTypeReference() );
        assertEquals( yearMonthTest.get( "date" ), DateUtils.parseDate( "2019-01" ) );
    }

    @Test
    public void testUnixEpochTimestamp()
        throws JsonProcessingException
    {
        Map<String, Date> unixEpochDateString = jsonMapper.readValue( createDateTest( "1575118800000" ),
            new DateMapTypeReference() );
        assertEquals( unixEpochDateString.get( "date" ), new Date( 1575118800000L ) );

        Map<String, Date> unixEpochDateLong = jsonMapper.readValue( createUnixEpochTest( 1575118800000L ),
            new DateMapTypeReference() );
        assertEquals( unixEpochDateLong.get( "date" ), new Date( 1575118800000L ) );
    }

    @Test
    public void testNullDate()
        throws JsonProcessingException
    {
        Map<String, Date> yearTest = jsonMapper.readValue( createDateTest( null ), new DateMapTypeReference() );
        assertNull( yearTest.get( "date" ) );
    }

    @Test // DHIS2-8582
    public void testSerializerUserWithUser()
        throws JsonProcessingException
    {
        User user = new User();
        user.setAutoFields();
        user.setUser( user );
        user.setLastUpdatedBy( user );

        String payload = jsonMapper.writeValueAsString( user );
        User testUser = jsonMapper.readValue( payload, User.class );

        assertNotNull( user.getUser() );
        assertNotNull( user.getLastUpdatedBy() );

        assertEquals( user.getUid(), testUser.getUid() );
        assertEquals( user.getUid(), user.getUser().getUid() );
        assertEquals( user.getUid(), user.getLastUpdatedBy().getUid() );
    }

    private String createDateTest( String str )
    {
        if ( str == null )
        {
            return "{\"date\": null }";
        }

        return String.format( "{\"date\": \"%s\"}", str );
    }

    private String createUnixEpochTest( long ts )
    {
        return String.format( "{\"date\": %d}", ts );
    }

    private static class DateMapTypeReference
        extends
        TypeReference<Map<String, Date>>
    {
    }
}
