package com.mass3d.patch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import com.mass3d.DhisSpringTest;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.common.ValueType;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementDomain;
import com.mass3d.dataelement.DataElementGroup;
import com.mass3d.user.User;
import com.mass3d.user.UserAccess;
import com.mass3d.user.UserGroup;
import com.mass3d.user.UserGroupAccess;
import com.mass3d.user.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

public class PatchServiceTest
    extends DhisSpringTest
{
    @Autowired
    private PatchService patchService;

    @Autowired
    private IdentifiableObjectManager manager;

    @Autowired
    private UserService _userService;

    @Autowired
    private ObjectMapper jsonMapper;

    @Override
    protected void setUpTest() throws Exception
    {
        userService = _userService;
    }

    @Test
    public void testUpdateName()
    {
        DataElement dataElement = createDataElement( 'A' );

        Patch patch = new Patch()
            .addMutation( new Mutation( "name", "Updated Name" ) );

        patchService.apply( patch, dataElement );

        assertEquals( "Updated Name", dataElement.getName() );
    }

    @Test
    public void testAddDataElementToGroup()
    {
        DataElementGroup dataElementGroup = createDataElementGroup( 'A' );
        DataElement deA = createDataElement( 'A' );
        DataElement deB = createDataElement( 'B' );

        manager.save( deA );
        manager.save( deB );

        assertTrue( dataElementGroup.getMembers().isEmpty() );

        Patch patch = new Patch()
            .addMutation( new Mutation( "name", "Updated Name" ) )
            .addMutation( new Mutation( "dataElements", Lists.newArrayList( deA.getUid(), deB.getUid() ) ) );

        patchService.apply( patch, dataElementGroup );

        assertEquals( "Updated Name", dataElementGroup.getName() );
        assertEquals( 2, dataElementGroup.getMembers().size() );
    }

    @Test
    public void testDeleteDataElementFromGroup()
    {
        DataElementGroup dataElementGroup = createDataElementGroup( 'A' );
        DataElement deA = createDataElement( 'A' );
        DataElement deB = createDataElement( 'B' );

        manager.save( deA );
        manager.save( deB );

        dataElementGroup.addDataElement( deA );
        dataElementGroup.addDataElement( deB );

        assertEquals( 2, dataElementGroup.getMembers().size() );

        Patch patch = new Patch()
            .addMutation( new Mutation( "name", "Updated Name" ) )
            .addMutation( new Mutation( "dataElements", Lists.newArrayList( deA.getUid() ), Mutation.Operation.DELETION ) );

        patchService.apply( patch, dataElementGroup );

        assertEquals( "Updated Name", dataElementGroup.getName() );
        assertEquals( 1, dataElementGroup.getMembers().size() );

        patch = new Patch()
            .addMutation( new Mutation( "dataElements", Lists.newArrayList( deB.getUid() ), Mutation.Operation.DELETION ) );

        patchService.apply( patch, dataElementGroup );

        assertTrue( dataElementGroup.getMembers().isEmpty() );
    }

    @Test
    public void testAddAggLevelsToDataElement()
    {
        DataElement dataElement = createDataElement( 'A' );
        assertTrue( dataElement.getAggregationLevels().isEmpty() );

        Patch patch = new Patch()
            .addMutation( new Mutation( "name", "Updated Name" ) )
            .addMutation( new Mutation( "aggregationLevels", 1 ) )
            .addMutation( new Mutation( "aggregationLevels", 2 ) );

        patchService.apply( patch, dataElement );

        assertEquals( 2, dataElement.getAggregationLevels().size() );
    }

    @Test
    public void testUpdateValueTypeEnumFromString()
    {
        DataElement dataElement = createDataElement( 'A' );
        assertTrue( dataElement.getAggregationLevels().isEmpty() );

        Patch patch = new Patch()
            .addMutation( new Mutation( "name", "Updated Name" ) )
            .addMutation( new Mutation( "domainType", "TRACKER" ) )
            .addMutation( new Mutation( "valueType", "BOOLEAN" ) );

        patchService.apply( patch, dataElement );

        assertEquals( DataElementDomain.TRACKER, dataElement.getDomainType() );
        assertEquals( ValueType.BOOLEAN, dataElement.getValueType() );
    }

    @Test
    public void testUpdateUserOnDataElement()
    {
        User user = createUser( 'A' );
        manager.save( user );

        createAndInjectAdminUser();

        DataElement dataElement = createDataElement( 'A' );
        manager.save( dataElement );

        Patch patch = new Patch()
            .addMutation( new Mutation( "name", "Updated Name" ) )
            .addMutation( new Mutation( "user", user.getUid() ) )
            .addMutation( new Mutation( "domainType", "TRACKER" ) )
            .addMutation( new Mutation( "valueType", "BOOLEAN" ) );

        patchService.apply( patch, dataElement );

        assertEquals( DataElementDomain.TRACKER, dataElement.getDomainType() );
        assertEquals( ValueType.BOOLEAN, dataElement.getValueType() );
        assertEquals( user.getUid(), dataElement.getUser().getUid() );
    }

    @Test
    public void testAddStringAggLevelsToDataElement()
    {
        DataElement dataElement = createDataElement( 'A' );
        assertTrue( dataElement.getAggregationLevels().isEmpty() );

        Patch patch = new Patch()
            .addMutation( new Mutation( "name", "Updated Name" ) )
            .addMutation( new Mutation( "aggregationLevels", "1" ) )
            .addMutation( new Mutation( "aggregationLevels", "abc" ) )
            .addMutation( new Mutation( "aggregationLevels", "def" ) );

        patchService.apply( patch, dataElement );
        assertTrue( dataElement.getAggregationLevels().isEmpty() );
    }

    @Test
    public void testUpdateUserCredentialsOnUser()
    {
        User user = createAndInjectAdminUser();
        assertEquals( "admin", user.getUserCredentials().getUsername() );

        Patch patch = new Patch()
            .addMutation( new Mutation( "userCredentials.username", "dhis" ) );

        patchService.apply( patch, user );

        assertEquals( "dhis", user.getUserCredentials().getUsername() );
    }

    @Test
    public void testSimpleDiff()
    {
        DataElement deA = createDataElement( 'A' );
        DataElement deB = createDataElement( 'B' );

        Patch patch = patchService.diff( new PatchParams( deA, deB ) );
        patchService.apply( patch, deA );

        assertEquals( deA.getName(), deB.getName() );
        assertEquals( deA.getShortName(), deB.getShortName() );
        assertEquals( deA.getDescription(), deB.getDescription() );
    }

    @Test
    public void testSimpleCollectionDiff()
    {
        DataElement deA = createDataElement( 'A' );
        DataElement deB = createDataElement( 'B' );

        deA.getAggregationLevels().add( 1 );
        deB.getAggregationLevels().add( 2 );
        deB.getAggregationLevels().add( 3 );

        Patch patch = patchService.diff( new PatchParams( deA, deB ) );

        checkCount( patch, "aggregationLevels", Mutation.Operation.ADDITION, 2 );
        checkCount( patch, "aggregationLevels", Mutation.Operation.DELETION, 1 );

        patchService.apply( patch, deA );

        assertEquals( deA.getName(), deB.getName() );
        assertEquals( deA.getShortName(), deB.getShortName() );
        assertEquals( deA.getDescription(), deB.getDescription() );
        assertEquals( deA.getAggregationLevels(), deB.getAggregationLevels() );
    }

    @Test
    public void testSimpleIdObjectCollectionDiff()
    {
        DataElement deA = createDataElement( 'A' );
        DataElement deB = createDataElement( 'B' );

        DataElementGroup degA = createDataElementGroup( 'C' );
        DataElementGroup degB = createDataElementGroup( 'D' );

        manager.save( degA );
        manager.save( degB );

        deA.getGroups().add( degA );
        manager.update( degA );

        deB.getGroups().add( degB );

        deA.getAggregationLevels().add( 1 );
        deA.getAggregationLevels().add( 2 );

        deB.getAggregationLevels().add( 2 );
        deB.getAggregationLevels().add( 3 );
        deB.getAggregationLevels().add( 4 );

        Patch patch = patchService.diff( new PatchParams( deA, deB ) );

        checkCount( patch, "dataElementGroups", Mutation.Operation.ADDITION, 1 );
        checkCount( patch, "dataElementGroups", Mutation.Operation.DELETION, 1 );

        checkCount( patch, "aggregationLevels", Mutation.Operation.ADDITION, 2 );
        checkCount( patch, "aggregationLevels", Mutation.Operation.DELETION, 1 );

        patchService.apply( patch, deA );

        assertEquals( deA.getName(), deB.getName() );
        assertEquals( deA.getShortName(), deB.getShortName() );
        assertEquals( deA.getDescription(), deB.getDescription() );
        assertEquals( deA.getAggregationLevels(), deB.getAggregationLevels() );
        assertEquals( deA.getGroups(), deB.getGroups() );
    }

    @Test
    public void testEmbeddedObjectEquality()
    {
        User adminUser = createAndInjectAdminUser();
        UserGroup userGroup = createUserGroup( 'A', Sets.newHashSet( adminUser ) );
        manager.save( userGroup );

        DataElement deA = createDataElement( 'A' );
        DataElement deB = createDataElement( 'B' );

        deA.getUserGroupAccesses().add( new UserGroupAccess( userGroup, "rw------" ) );
        deA.getUserAccesses().add( new UserAccess( adminUser, "rw------" ) );

        deB.getUserGroupAccesses().add( new UserGroupAccess( userGroup, "rw------" ) );
        deB.getUserAccesses().add( new UserAccess( adminUser, "rw------" ) );

        patchService.diff( new PatchParams( deA, deB ) );
    }

    @Test
    public void testEmbeddedObjectCollectionDiff()
    {
        User adminUser = createAndInjectAdminUser();
        UserGroup userGroup = createUserGroup( 'A', Sets.newHashSet( adminUser ) );
        manager.save( userGroup );

        DataElement deA = createDataElement( 'A' );
        DataElement deB = createDataElement( 'B' );

        deA.getAggregationLevels().add( 1 );
        deB.getAggregationLevels().add( 1 );
        deB.getAggregationLevels().add( 2 );
        deB.getAggregationLevels().add( 3 );

        deB.getUserGroupAccesses().add( new UserGroupAccess( userGroup, "rw------" ) );
        deB.getUserAccesses().add( new UserAccess( adminUser, "rw------" ) );

        Patch patch = patchService.diff( new PatchParams( deA, deB ) );
        patchService.apply( patch, deA );

        assertEquals( deA.getName(), deB.getName() );
        assertEquals( deA.getShortName(), deB.getShortName() );
        assertEquals( deA.getDescription(), deB.getDescription() );
        assertEquals( deA.getAggregationLevels(), deB.getAggregationLevels() );
        assertEquals( deA.getUserGroupAccesses(), deB.getUserGroupAccesses() );
        assertEquals( deA.getUserAccesses(), deB.getUserAccesses() );
    }

    @Test
    public void testPatchFromJsonNode1()
    {
        JsonNode jsonNode = loadJsonNodeFromFile( "patch/simple.json" );
        DataElement dataElement = createDataElement( 'A' );

        Patch patch = patchService.diff( new PatchParams( jsonNode ) );
        assertEquals( 2, patch.getMutations().size() );

        patchService.apply( patch, dataElement );

        assertEquals( dataElement.getName(), "Updated Name" );
        assertEquals( dataElement.getShortName(), "Updated Short Name" );
    }

    @Test
    public void testPatchFromJsonNode2()
    {
        JsonNode jsonNode = loadJsonNodeFromFile( "patch/id-collection.json" );
        DataElement dataElement = createDataElement( 'A' );

        DataElementGroup degA = createDataElementGroup( 'C' );
        DataElementGroup degB = createDataElementGroup( 'D' );

        manager.save( degA );
        manager.save( degB );

        Patch patch = patchService.diff( new PatchParams( jsonNode ) );
        patchService.apply( patch, dataElement );

        assertEquals( dataElement.getName(), "Updated Name" );
        assertEquals( dataElement.getShortName(), "Updated Short Name" );
        assertEquals( 2, dataElement.getGroups().size() );
    }

    @Test
    public void testPatchFromJsonNode3()
    {
        JsonNode jsonNode = loadJsonNodeFromFile( "patch/complex.json" );
        patchService.diff( new PatchParams( jsonNode ) );
    }

    private JsonNode loadJsonNodeFromFile( String path )
    {
        try
        {
            InputStream inputStream = new ClassPathResource( path ).getInputStream();
            return jsonMapper.readTree( inputStream );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

        return null;
    }

    private void checkCount( Patch patch, String name, Mutation.Operation operation, int expected )
    {
        int count = 0;

        for ( Mutation mutation : patch.getMutations() )
        {
            if ( mutation.getOperation() == operation && mutation.getPath().equals( name ) )
            {
                if ( Collection.class.isInstance( mutation.getValue() ) )
                {
                    count += ((Collection<?>) mutation.getValue()).size();
                }
                else
                {
                    count++;
                }
            }
        }

        assertEquals( "Did not find " + expected + " mutations of type " + operation + " on property " + name, expected, count );
    }
}
