package com.mass3d.schema;

import static org.hamcrest.Matchers.contains;

import com.mass3d.schema.Schema;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.mass3d.common.MetadataObject;
import com.mass3d.common.SecondaryMetadataObject;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link Schema}.
 *
 */
public class SchemaTest
{
    private List<Authority> authorities;

    @Before
    public void setUp()
    {
        authorities = new ArrayList<>();
        authorities.add( new Authority( AuthorityType.CREATE, Arrays.asList( "x1", "x2" ) ) );
        authorities.add( new Authority( AuthorityType.CREATE, Arrays.asList( "y1", "y2" ) ) );
        authorities.add( new Authority( AuthorityType.DELETE, Arrays.asList( "z1", "z2" ) ) );
    }

    @Test
    public void isSecondaryMetadataObject()
    {
        Assert.assertTrue( new Schema( SecondaryMetadata.class, "singular", "plural" ).isSecondaryMetadata() );
    }

    @Test
    public void isSecondaryMetadataObjectMetadata()
    {
        Assert.assertTrue( new Schema( SecondaryMetadata.class, "singular", "plural" ).isMetadata() );
    }

    @Test
    public void isSecondaryMetadataObjectNot()
    {
        Assert.assertFalse( new Schema( Metadata.class, "singular", "plural" ).isSecondaryMetadata() );
    }

    @Test
    public void testAuthorityByType()
    {
        final Schema schema = new Schema( SecondaryMetadata.class, "singular", "plural" );
        schema.setAuthorities( authorities );

        List<String> list1 = schema.getAuthorityByType( AuthorityType.CREATE );
        Assert.assertThat( list1, contains( "x1", "x2", "y1", "y2" ) );

        List<String> list2 = schema.getAuthorityByType( AuthorityType.CREATE );
        Assert.assertThat( list2, contains( "x1", "x2", "y1", "y2" ) );
        Assert.assertSame( list1, list2 );
    }

    @Test
    public void testAuthorityByTypeDifferent()
    {
        final Schema schema = new Schema( SecondaryMetadata.class, "singular", "plural" );
        schema.setAuthorities( authorities );

        List<String> list1 = schema.getAuthorityByType( AuthorityType.CREATE );
        Assert.assertThat( list1, contains( "x1", "x2", "y1", "y2" ) );

        List<String> list3 = schema.getAuthorityByType( AuthorityType.DELETE );
        Assert.assertThat( list3, contains( "z1", "z2" ) );

        List<String> list2 = schema.getAuthorityByType( AuthorityType.CREATE );
        Assert.assertThat( list2, contains( "x1", "x2", "y1", "y2" ) );
        Assert.assertSame( list1, list2 );
    }

    @Test
    public void testAuthorityByTypeNotFound()
    {
        final Schema schema = new Schema( SecondaryMetadata.class, "singular", "plural" );
        schema.setAuthorities( authorities );

        List<String> list1 = schema.getAuthorityByType( AuthorityType.CREATE_PRIVATE );
        Assert.assertTrue( list1.isEmpty() );

        List<String> list2 = schema.getAuthorityByType( AuthorityType.CREATE_PRIVATE );
        Assert.assertTrue( list2.isEmpty() );
        Assert.assertSame( list1, list2 );
    }

    @Test
    public void testAuthorityByTypeReset()
    {
        final Schema schema = new Schema( SecondaryMetadata.class, "singular", "plural" );
        schema.setAuthorities( authorities );

        List<String> list1 = schema.getAuthorityByType( AuthorityType.CREATE );
        Assert.assertThat( list1, contains( "x1", "x2", "y1", "y2" ) );

        authorities.add( new Authority( AuthorityType.CREATE, Arrays.asList( "a1", "a2" ) ) );
        schema.setAuthorities( authorities );

        List<String> list2 = schema.getAuthorityByType( AuthorityType.CREATE );
        Assert.assertThat( list2, contains( "x1", "x2", "y1", "y2", "a1", "a2" ) );
        Assert.assertNotSame( list1, list2 );
    }

    private static class SecondaryMetadata implements SecondaryMetadataObject
    {
    }

    private static class Metadata implements MetadataObject
    {
    }
}