package com.mass3d.hibernate.jsonb.type;

import java.util.HashSet;
import java.util.Set;
import org.hamcrest.Matchers;
import com.mass3d.translation.Translation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link JsonSetBinaryType}.
 *
 */
public class JsonSetBinaryTypeTest
{
    private JsonSetBinaryType jsonBinaryType;

    private Set<Translation> translations;

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

        translations = new HashSet<>();
        translations.add( translation1 );
        translations.add( translation2 );

        jsonBinaryType = new JsonSetBinaryType();
        jsonBinaryType.init( Translation.class );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void deepCopy()
    {
        final Set<Translation> result = (Set<Translation>) jsonBinaryType.deepCopy( translations );
        Assert.assertNotSame( translations, result );
        Assert.assertThat( result, Matchers.containsInAnyOrder( translation1, translation2 ) );
    }

    @Test
    public void deepCopyNull()
    {
        Assert.assertNull( jsonBinaryType.deepCopy( null ) );
    }
}