package com.mass3d.render;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.mass3d.DhisSpringTest;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dataelement.DataElement;
import com.mass3d.metadata.version.MetadataVersion;
import com.mass3d.metadata.version.VersionType;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

public class RenderServiceTest
    extends DhisSpringTest
{
    @Autowired
    private RenderService renderService;

    @Test
    public void testParseJsonMetadata() throws IOException
    {
        Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> map = renderService.fromMetadata(
            new ClassPathResource( "render/metadata.json" ).getInputStream(), RenderFormat.JSON );

        assertTrue( map.containsKey( DataElement.class ) );
        assertEquals( 3, map.get( DataElement.class ).size() );
        assertEquals( "DataElementA", map.get( DataElement.class ).get( 0 ).getName() );
        assertEquals( "DataElementB", map.get( DataElement.class ).get( 1 ).getName() );
        assertEquals( "DataElementC", map.get( DataElement.class ).get( 2 ).getName() );
        assertEquals( "deabcdefghA", map.get( DataElement.class ).get( 0 ).getUid() );
        assertEquals( "deabcdefghB", map.get( DataElement.class ).get( 1 ).getUid() );
        assertEquals( "deabcdefghC", map.get( DataElement.class ).get( 2 ).getUid() );
    }

    @Test
    @Ignore // ignoring since Jackson can't handle parsing of XML as trees
    public void testParseXmlMetadata() throws IOException
    {
        Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> map = renderService.fromMetadata(
            new ClassPathResource( "render/metadata.xml" ).getInputStream(), RenderFormat.XML );

        assertTrue( map.containsKey( DataElement.class ) );
        assertEquals( 3, map.get( DataElement.class ).size() );
        assertEquals( "DataElementA", map.get( DataElement.class ).get( 0 ).getName() );
        assertEquals( "DataElementB", map.get( DataElement.class ).get( 1 ).getName() );
        assertEquals( "DataElementC", map.get( DataElement.class ).get( 2 ).getName() );
        assertEquals( "deabcdefghA", map.get( DataElement.class ).get( 0 ).getUid() );
        assertEquals( "deabcdefghB", map.get( DataElement.class ).get( 1 ).getUid() );
        assertEquals( "deabcdefghC", map.get( DataElement.class ).get( 2 ).getUid() );
    }

    @Test
    public void testfromMetadataVersion_should_give_empty_list_if_format_is_not_JSON() throws IOException
    {
        assertEquals( 0, renderService.fromMetadataVersion( null, null ).size() );
    }

    @Test
    public void testfromMetadataVersion_should_generate_versions_from_json_stream() throws IOException
    {
        String jsonString = "{" +
            "\"metadataversions\":  [" +
            "{\"name\" : \"version1\",\"type\" : \"ATOMIC\"}," +
            "{\"name\" : \"version2\",\"type\" : \"ATOMIC\"}," +
            "{\"name\" : \"version3\",\"type\" : \"ATOMIC\"}" +
            "]" +
            "}";

        ByteArrayInputStream bais = new ByteArrayInputStream( jsonString.getBytes() );
        List<MetadataVersion> metadataVersions = renderService.fromMetadataVersion( bais, RenderFormat.JSON );
        assertEquals( 3, metadataVersions.size() );
        assertEquals( "version1", metadataVersions.get( 0 ).getName() );
        assertEquals( "version2", metadataVersions.get( 1 ).getName() );
        assertEquals( "version3", metadataVersions.get( 2 ).getName() );
        assertEquals( VersionType.ATOMIC, metadataVersions.get( 0 ).getType() );
        assertEquals( VersionType.ATOMIC, metadataVersions.get( 1 ).getType() );
        assertEquals( VersionType.ATOMIC, metadataVersions.get( 2 ).getType() );
    }

    @Test
    public void testEmptyStringShouldBeDeserializedAsNull() throws IOException
    {
        String json = "{\"a\": null, \"b\": \"\", \"c\": \"abc\"}";
        DeserializeTest deserializeTest = renderService.fromJson( json, DeserializeTest.class );

        assertNull( deserializeTest.getA() );
        assertNull( deserializeTest.getB() );
        assertNotNull( deserializeTest.getC() );
    }

    @Test
    public void testShouldSupportMultipleDateFormats() throws IOException
    {
        Date y2011 = renderService.fromJson( "{\"d\": \"2011\"}", DeserializeTest.class ).getD();
        Date y201105 = renderService.fromJson( "{\"d\": \"2011-05\"}", DeserializeTest.class ).getD();
        Date y2012 = renderService.fromJson( "{\"d\": \"2012\"}", DeserializeTest.class ).getD();
        Date y2013 = renderService.fromJson( "{\"d\": \"2013\"}", DeserializeTest.class ).getD();
        Date y201312 = renderService.fromJson( "{\"d\": \"2013-12\"}", DeserializeTest.class ).getD();

        assertNotNull( y2011 );
        assertNotNull( y201105 );
        assertNotNull( y2012 );
        assertNotNull( y2013 );
        assertNotNull( y201312 );

        verifyCalendar( y2011, 2011, null );
        verifyCalendar( y201105, 2011, 4 );
        verifyCalendar( y2012, 2012, null );
        verifyCalendar( y2013, 2013, null );
        verifyCalendar( y201312, 2013, 11 );
    }

    private void verifyCalendar( Date date, int year, Integer month )
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( date );

        assertEquals( calendar.get( Calendar.YEAR ), year );

        if ( month != null )
        {
            assertEquals( calendar.get( Calendar.MONTH ), (int) month );
        }
    }
}
