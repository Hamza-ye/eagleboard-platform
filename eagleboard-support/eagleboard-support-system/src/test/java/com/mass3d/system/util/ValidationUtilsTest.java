package com.mass3d.system.util;

import static com.mass3d.system.util.ValidationUtils.bboxIsValid;
import static com.mass3d.system.util.ValidationUtils.coordinateIsValid;
import static com.mass3d.system.util.ValidationUtils.dataValueIsValid;
import static com.mass3d.system.util.ValidationUtils.dataValueIsZeroAndInsignificant;
import static com.mass3d.system.util.ValidationUtils.emailIsValid;
import static com.mass3d.system.util.ValidationUtils.expressionIsValidSQl;
import static com.mass3d.system.util.ValidationUtils.getLatitude;
import static com.mass3d.system.util.ValidationUtils.getLongitude;
import static com.mass3d.system.util.ValidationUtils.isValidHexColor;
import static com.mass3d.system.util.ValidationUtils.normalizeBoolean;
import static com.mass3d.system.util.ValidationUtils.passwordIsValid;
import static com.mass3d.system.util.ValidationUtils.usernameIsValid;
import static com.mass3d.system.util.ValidationUtils.uuidIsValid;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.mass3d.analytics.AggregationType;
import com.mass3d.common.CodeGenerator;
import com.mass3d.common.ValueType;
import com.mass3d.dataelement.DataElement;
import org.junit.Test;

public class ValidationUtilsTest
{
    @Test
    public void testCoordinateIsValid()
    {
        assertTrue( coordinateIsValid( "[+37.99034,-28.94221]" ) );
        assertTrue( coordinateIsValid( "[37.99034,-28.94221]" ) );
        assertTrue( coordinateIsValid( "[+37.99034,28.94221]" ) );
        assertTrue( coordinateIsValid( "[170.99034,78.94221]" ) );
        assertTrue( coordinateIsValid( "[-167,-28.94221]" ) );
        assertTrue( coordinateIsValid( "[37.99034,28]" ) );

        assertFalse( coordinateIsValid( "23.34343,56.3232" ) );
        assertFalse( coordinateIsValid( "23.34343 56.3232" ) );
        assertFalse( coordinateIsValid( "[23.34f43,56.3232]" ) );
        assertFalse( coordinateIsValid( "23.34343,56.323.2" ) );
        assertFalse( coordinateIsValid( "[23.34343,56..3232]" ) );
        assertFalse( coordinateIsValid( "[++37,-28.94221]" ) );
        assertFalse( coordinateIsValid( "S-0.27726 E37.08472" ) );
        assertFalse( coordinateIsValid( null ) );

        assertFalse( coordinateIsValid( "-185.12345,45.45423" ) );
        assertFalse( coordinateIsValid( "192.56789,-45.34332" ) );
        assertFalse( coordinateIsValid( "140.34,92.23323" ) );
        assertFalse( coordinateIsValid( "123.34,-94.23323" ) );
        assertFalse( coordinateIsValid( "000.34,-94.23323" ) );
        assertFalse( coordinateIsValid( "123.34,-00.23323" ) );
    }

    @Test
    public void testBboxIsValid()
    {
        assertTrue( bboxIsValid( "-13.2682125,7.3721619,-10.4261178,9.904012" ) );
        assertTrue( bboxIsValid( "12.26821,-23.3721,13.4261,-21.904" ) );
        assertTrue( bboxIsValid( "4,-23.37,5,-24.904" ) );
        assertTrue( bboxIsValid( "2.23, -23.37, 5.22, -24.90" ) );
        assertTrue( bboxIsValid( "-179.234,-89.342,178.323,88.135" ) );

        assertFalse( bboxIsValid( "[12.23,14.41,34.12,12.45]" ) );
        assertFalse( bboxIsValid( "22,23,14,41,34,11,11,41" ) );
        assertFalse( bboxIsValid( "22,23.14,41.34,11.11,41" ) );
        assertFalse( bboxIsValid( "-181.234,-89.342,178.323,88.135" ) );
        assertFalse( bboxIsValid( "-179.234,-92.342,178.323,88.135" ) );
        assertFalse( bboxIsValid( "-179.234,-89.342,185.323,88.135" ) );
        assertFalse( bboxIsValid( "-179.234,-89.342,178.323,94.135" ) );
    }

    @Test
    public void testGetLongitude()
    {
        assertEquals( "+37.99034", getLongitude( "[+37.99034,-28.94221]" ) );
        assertEquals( "37.99034", getLongitude( "[37.99034,28.94221]" ) );
        assertNull( getLongitude( "23.34343,56.3232" ) );
        assertNull( getLongitude( null ) );
    }

    @Test
    public void testGetLatitude()
    {
        assertEquals( "-28.94221", getLatitude( "[+37.99034,-28.94221]" ) );
        assertEquals( "28.94221", getLatitude( "[37.99034,28.94221]" ) );
        assertNull( getLatitude( "23.34343,56.3232" ) );
        assertNull( getLatitude( null ) );
    }

    @Test
    public void testPasswordIsValid()
    {
        assertFalse( passwordIsValid( "Johnd1" ) );
        assertFalse( passwordIsValid( "johndoe1" ) );
        assertFalse( passwordIsValid( "Johndoedoe" ) );
        assertTrue( passwordIsValid( "Johndoe1" ) );
    }

    @Test
    public void testEmailIsValid()
    {
        assertFalse( emailIsValid( "john@doe" ) );
        assertTrue( emailIsValid( "john@doe.com" ) );
    }

    @Test
    public void testUuidIsValid()
    {
        assertTrue( uuidIsValid( "0b976c48-4577-437b-bba6-794d0e7ebde0" ) );
        assertTrue( uuidIsValid( "38052fd0-8c7a-4330-ac45-2c53b3a41a78" ) );
        assertTrue( uuidIsValid( "50be5898-2413-465f-91b9-aced950fc3ab" ) );

        assertFalse( uuidIsValid( "Jjg3j3-412-1435-342-jajg8234f" ) );
        assertFalse( uuidIsValid( "6cafdc73_2ca4_4c52-8a0a-d38adec33b24" ) );
        assertFalse( uuidIsValid( "e1809673dbf3482d8f84e493c65f74d9" ) );
    }

    @Test
    public void testUsernameIsValid()
    {
        assertTrue( usernameIsValid( "johnmichaeldoe" ) );
        assertTrue( usernameIsValid( "ted@johnson.com" ) );
        assertTrue( usernameIsValid( "harry@gmail.com" ) );

        assertFalse( usernameIsValid( null ) );
        assertFalse( usernameIsValid( CodeGenerator.generateCode( 400 ) ) );
    }

    @Test
    public void testDataValueIsZeroAndInsignificant()
    {
        DataElement de = new DataElement( "DEA" );
        de.setValueType( ValueType.INTEGER );
        de.setAggregationType( AggregationType.SUM );

        assertTrue( dataValueIsZeroAndInsignificant( "0", de ) );

        de.setAggregationType( AggregationType.AVERAGE_SUM_ORG_UNIT );
        assertFalse( dataValueIsZeroAndInsignificant( "0", de ) );
    }

    @Test
    public void testDataValueIsValid()
    {
        DataElement de = new DataElement( "DEA" );
        de.setValueType( ValueType.INTEGER );

        assertNull( dataValueIsValid( null, de ) );
        assertNull( dataValueIsValid( "", de ) );

        assertNull( dataValueIsValid( "34", de ) );
        assertNotNull( dataValueIsValid( "Yes", de ) );

        de.setValueType( ValueType.NUMBER );

        assertNull( dataValueIsValid( "3.7", de ) );
        assertNotNull( dataValueIsValid( "No", de ) );

        de.setValueType( ValueType.INTEGER_POSITIVE );

        assertNull( dataValueIsValid( "3", de ) );
        assertNotNull( dataValueIsValid( "-4", de ) );

        de.setValueType( ValueType.INTEGER_ZERO_OR_POSITIVE );

        assertNull( dataValueIsValid( "3", de ) );
        assertNotNull( dataValueIsValid( "-4", de ) );

        de.setValueType( ValueType.INTEGER_NEGATIVE );

        assertNull( dataValueIsValid( "-3", de ) );
        assertNotNull( dataValueIsValid( "4", de ) );

        de.setValueType( ValueType.TEXT );

        assertNull( dataValueIsValid( "0", de ) );

        de.setValueType( ValueType.BOOLEAN );

        assertNull( dataValueIsValid( "true", de ) );
        assertNull( dataValueIsValid( "false", de ) );
        assertNull( dataValueIsValid( "FALSE", de ) );
        assertNotNull( dataValueIsValid( "yes", de ) );

        de.setValueType( ValueType.TRUE_ONLY );

        assertNull( dataValueIsValid( "true", de ) );
        assertNull( dataValueIsValid( "TRUE", de ) );
        assertNotNull( dataValueIsValid( "false", de ) );

        de.setValueType( ValueType.DATE );
        assertNull( dataValueIsValid( "2013-04-01", de ) );
        assertNotNull( dataValueIsValid( "2012304-01", de ) );
        assertNotNull( dataValueIsValid( "Date", de ) );

        de.setValueType( ValueType.DATETIME );
        assertNull( dataValueIsValid( "2013-04-01T11:00:00.000Z", de ) );
        assertNotNull( dataValueIsValid( "2013-04-01", de ) );
        assertNotNull( dataValueIsValid( "abcd", de ) );
    }

    @Test
    public void testIsValidHexColor()
    {
        assertFalse( isValidHexColor( "abcpqr" ) );
        assertFalse( isValidHexColor( "#qwerty" ) );
        assertFalse( isValidHexColor( "FFAB#O" ) );

        assertTrue( isValidHexColor( "#FF0" ) );
        assertTrue( isValidHexColor( "#FF0000" ) );
        assertTrue( isValidHexColor( "FFFFFF" ) );
        assertTrue( isValidHexColor( "ffAAb4" ) );
        assertTrue( isValidHexColor( "#4a6" ) );
        assertTrue( isValidHexColor( "abc" ) );
    }

    @Test
    public void testExpressionIsValidSQl()
    {
        assertFalse( expressionIsValidSQl( "10 == 10; delete from table" ) );
        assertFalse( expressionIsValidSQl( "select from table" ) );

        assertTrue( expressionIsValidSQl( "\"abcdef12345\" < 30" ) );
        assertTrue( expressionIsValidSQl( "\"abcdef12345\" >= \"bcdefg23456\"" ) );
        assertTrue( expressionIsValidSQl( "\"DO0v7fkhUNd\" > -30000 and \"DO0v7fkhUNd\" < 30000" ) );
        assertTrue( expressionIsValidSQl( "\"oZg33kd9taw\" == 'Female'" ) );
        assertTrue( expressionIsValidSQl( "\"oZg33kd9taw\" == 'Female' and \"qrur9Dvnyt5\" <= 5" ) );
    }

    @Test
    public void testNormalizeBoolean()
    {
        assertEquals( "true", normalizeBoolean( "1", ValueType.BOOLEAN ) );
        assertEquals( "true", normalizeBoolean( "T", ValueType.BOOLEAN ) );
        assertEquals( "true", normalizeBoolean( "true", ValueType.BOOLEAN ) );
        assertEquals( "true", normalizeBoolean( "TRUE", ValueType.BOOLEAN ) );
        assertEquals( "true", normalizeBoolean( "t", ValueType.BOOLEAN ) );

        assertEquals( "test", normalizeBoolean( "test", ValueType.TEXT ) );

        assertEquals( "false", normalizeBoolean( "0", ValueType.BOOLEAN ) );
        assertEquals( "false", normalizeBoolean( "f", ValueType.BOOLEAN ) );
        assertEquals( "false", normalizeBoolean( "False", ValueType.BOOLEAN ) );
        assertEquals( "false", normalizeBoolean( "FALSE", ValueType.BOOLEAN ) );
        assertEquals( "false", normalizeBoolean( "F", ValueType.BOOLEAN ) );
    }
}
