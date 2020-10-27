package com.mass3d.dxf2.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import com.mass3d.DhisSpringTest;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementGroup;
import com.mass3d.query.Disjunction;
import com.mass3d.query.Query;
import com.mass3d.query.Restrictions;
import com.mass3d.schema.SchemaService;
import com.mass3d.user.User;
import com.mass3d.user.UserAccess;
import com.mass3d.user.UserGroup;
import com.mass3d.user.UserGroupAccess;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MetadataExportServiceTest
    extends DhisSpringTest
{
    @Autowired
    private MetadataExportService metadataExportService;

    @Autowired
    private IdentifiableObjectManager manager;

    @Autowired
    private SchemaService schemaService;

    @Test
    public void testValidate()
    {
        MetadataExportParams params = new MetadataExportParams();
        metadataExportService.validate( params );
    }

    @Test
    public void testMetadataExport()
    {
        DataElementGroup deg1 = createDataElementGroup( 'A' );
        DataElement de1 = createDataElement( 'A' );
        DataElement de2 = createDataElement( 'B' );
        DataElement de3 = createDataElement( 'C' );

        manager.save( de1 );
        manager.save( de2 );
        manager.save( de3 );

        User user = createUser( 'A' );
        manager.save( user );

        deg1.addDataElement( de1 );
        deg1.addDataElement( de2 );
        deg1.addDataElement( de3 );

        deg1.setUser( user );
        manager.save( deg1 );

        MetadataExportParams params = new MetadataExportParams();
        Map<Class<? extends IdentifiableObject>, List<? extends IdentifiableObject>> metadata = metadataExportService.getMetadata( params );

        assertEquals( 1, metadata.get( User.class ).size() );
        assertEquals( 1, metadata.get( DataElementGroup.class ).size() );
        assertEquals( 3, metadata.get( DataElement.class ).size() );
    }

    @Test
    public void testMetadataExportWithCustomClasses()
    {
        DataElementGroup deg1 = createDataElementGroup( 'A' );
        DataElement de1 = createDataElement( 'A' );
        DataElement de2 = createDataElement( 'B' );
        DataElement de3 = createDataElement( 'C' );

        manager.save( de1 );
        manager.save( de2 );
        manager.save( de3 );

        User user = createUser( 'A' );
        manager.save( user );

        deg1.addDataElement( de1 );
        deg1.addDataElement( de2 );
        deg1.addDataElement( de3 );

        deg1.setUser( user );
        manager.save( deg1 );

        MetadataExportParams params = new MetadataExportParams();
        params.addClass( DataElement.class );

        Map<Class<? extends IdentifiableObject>, List<? extends IdentifiableObject>> metadata = metadataExportService.getMetadata( params );

        assertFalse( metadata.containsKey( User.class ) );
        assertFalse( metadata.containsKey( DataElementGroup.class ) );
        assertTrue( metadata.containsKey( DataElement.class ) );

        assertEquals( 3, metadata.get( DataElement.class ).size() );
    }

    @Test
    public void testMetadataExportWithCustomQueries()
    {
        DataElementGroup deg1 = createDataElementGroup( 'A' );
        DataElement de1 = createDataElement( 'A' );
        DataElement de2 = createDataElement( 'B' );
        DataElement de3 = createDataElement( 'C' );

        manager.save( de1 );
        manager.save( de2 );
        manager.save( de3 );

        User user = createUser( 'A' );
        manager.save( user );

        deg1.addDataElement( de1 );
        deg1.addDataElement( de2 );
        deg1.addDataElement( de3 );

        deg1.setUser( user );
        manager.save( deg1 );

        Query deQuery = Query.from( schemaService.getDynamicSchema( DataElement.class ) );

        Disjunction disjunction = deQuery.disjunction();
        disjunction.add( Restrictions.eq( "id", de1.getUid() ) );
        disjunction.add( Restrictions.eq( "id", de2.getUid() ) );

        deQuery.add( disjunction );

        Query degQuery = Query.from( schemaService.getDynamicSchema( DataElementGroup.class ) );
        degQuery.add( Restrictions.eq( "id", "INVALID UID" ) );

        MetadataExportParams params = new MetadataExportParams();
        params.addQuery( deQuery );
        params.addQuery( degQuery );

        Map<Class<? extends IdentifiableObject>, List<? extends IdentifiableObject>> metadata = metadataExportService.getMetadata( params );

        assertFalse( metadata.containsKey( User.class ) );
        assertFalse( metadata.containsKey( DataElementGroup.class ) );
        assertTrue( metadata.containsKey( DataElement.class ) );

        assertEquals( 2, metadata.get( DataElement.class ).size() );
    }

    @Test
    public void testSkipSharing()
    {
        MetadataExportParams params = new MetadataExportParams();
        params.setSkipSharing( true );

        User user = createUser( 'A' );
        UserGroup group = createUserGroup( 'A', Sets.newHashSet( user ));
        DataElement de1 = createDataElement( 'A' );
        DataElement de2 = createDataElement( 'B' );
        DataElement de3 = createDataElement( 'C' );
        DataElement de4 = createDataElement( 'D' );
        DataElement de5 = createDataElement( 'E' );

        de1.setUserAccesses( Sets.newHashSet( new UserAccess( user, "rwrwrwrw" ) ) );
        de2.setPublicAccess( "rwrwrwrw" );
        de3.setUser( user );
        de4.setUserGroupAccesses( Sets.newHashSet( new UserGroupAccess( group, "rwrwrwrw" ) ) );
        de5.setExternalAccess( true );

        manager.save( user );
        manager.save( group );
        manager.save( de1 );
        manager.save( de2 );
        manager.save( de3 );
        manager.save( de4 );
        manager.save( de5 );

        Map<Class<? extends IdentifiableObject>, List<? extends IdentifiableObject>> metadata = metadataExportService.getMetadata( params );

        assertEquals( 5, metadata.get( DataElement.class ).size() );

        metadata.get( DataElement.class ).stream()
            .forEach( element -> checkSharingFields( element ) );
    }

    private void checkSharingFields( IdentifiableObject object )
    {
        assertTrue( object.getUserAccesses().isEmpty() );
        assertEquals( "--------", object.getPublicAccess() );
        //assertNull( object.getUser() );
        assertTrue( object.getUserGroupAccesses().isEmpty() );
        //assertFalse( object.getExternalAccess() );
    }
}
