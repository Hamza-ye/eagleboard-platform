package com.mass3d.hibernate.jsonb.type;

import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;
import com.mass3d.translation.Translation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link JsonListBinaryType}.
 *
 */
public class JsonListBinaryTypeTest
{
    private JsonListBinaryType jsonBinaryType;

    private List<Translation> translations;

    private Translation translation1;

    private Translation translation2;

    @Before
    public void setUp()
    {
        translation1 = new Translation();
        translation1.setLocale( "en" );
        translation1.setValue( "English Test 1" );

        translation2 = new Translation();
        translation2.setLocale( "no" );
        translation2.setValue( "Norwegian Test 1" );

        translations = new ArrayList<>();
        translations.add( translation1 );
        translations.add( translation2 );

        jsonBinaryType = new JsonListBinaryType();
        jsonBinaryType.init( Translation.class );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void deepCopy()
    {
        final List<Translation> result = (List<Translation>) jsonBinaryType.deepCopy( translations );
        Assert.assertNotSame( translations, result );
        Assert.assertThat( result, Matchers.contains( translation1, translation2 ) );
    }

    @Test
    public void deepCopyNull()
    {
        Assert.assertNull( jsonBinaryType.deepCopy( null ) );
    }
}