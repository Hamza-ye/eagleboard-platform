package com.mass3d.hibernate.jsonb.type;

import com.mass3d.translation.Translation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link JsonBinaryType}.
 *
 */
public class JsonBinaryTypeTest
{
    private JsonBinaryType jsonBinaryType;

    private Translation translation1;

    @Before
    public void setUp()
    {
        translation1 = new Translation();
        translation1.setLocale( "en" );
        translation1.setValue( "English Test 1" );

        jsonBinaryType = new JsonBinaryType();
        jsonBinaryType.init( Translation.class );
    }

    @Test
    public void deepCopy()
    {
        final Translation result = (Translation) jsonBinaryType.deepCopy( translation1 );
        Assert.assertNotSame( translation1, result );
        Assert.assertEquals( translation1, result );
    }

    @Test
    public void deepCopyNull()
    {
        Assert.assertNull( jsonBinaryType.deepCopy( null ) );
    }
}
