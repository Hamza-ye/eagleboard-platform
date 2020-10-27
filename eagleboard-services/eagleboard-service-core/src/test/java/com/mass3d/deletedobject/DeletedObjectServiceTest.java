package com.mass3d.deletedobject;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.mass3d.todotask.TodoTask;
import java.util.List;
import com.mass3d.IntegrationTestBase;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.dataelement.DataElement;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DeletedObjectServiceTest
    extends IntegrationTestBase
{
    @Autowired
    private DeletedObjectService deletedObjectService;

    @Autowired
    private IdentifiableObjectManager manager;

    private DeletedObject elementA = new DeletedObject( createDataElement( 'A' ) );

    private DeletedObject elementB = new DeletedObject( createDataElement( 'B' ) );

    private DeletedObject elementC = new DeletedObject( createDataElement( 'C' ) );

    private DeletedObject elementD = new DeletedObject( createDataElement( 'D' ) );

    private DeletedObject elementE = new DeletedObject( createDataElement( 'E' ) );

    @Test
    public void testAddDeletedObject()
    {
        deletedObjectService.addDeletedObject( elementA );
        deletedObjectService.addDeletedObject( elementB );
        deletedObjectService.addDeletedObject( elementC );

        assertEquals( 3, deletedObjectService.countDeletedObjects() );
    }

    @Test
    public void testGetDeletedObject()
    {
        DeletedObjectQuery deletedObjectQuery = new DeletedObjectQuery();
        deletedObjectQuery.setTotal( 5 );
        deletedObjectQuery.setPageSize( 2 );

        deletedObjectService.addDeletedObject( elementA );
        deletedObjectService.addDeletedObject( elementB );
        deletedObjectService.addDeletedObject( elementC );
        deletedObjectService.addDeletedObject( elementD );
        deletedObjectService.addDeletedObject( elementE );

        deletedObjectQuery.setPage( 1 );
        List<DeletedObject> firstPageDeletedObjects = deletedObjectService.getDeletedObjects( deletedObjectQuery );
        deletedObjectQuery.setPage( 2 );
        List<DeletedObject> secondPageDeletedObjects = deletedObjectService.getDeletedObjects( deletedObjectQuery );
        deletedObjectQuery.setPage( 3 );
        List<DeletedObject> thirdPageDeletedObjects = deletedObjectService.getDeletedObjects( deletedObjectQuery );

        assertEquals( 5, deletedObjectService.countDeletedObjects() );
        assertEquals( 2, firstPageDeletedObjects.size() );
        assertEquals( 2, secondPageDeletedObjects.size() );
        assertEquals( 1, thirdPageDeletedObjects.size() );
        assertThat( firstPageDeletedObjects, contains( elementA, elementB ) );
        assertThat( secondPageDeletedObjects, contains( elementC, elementD ) );
        assertThat( thirdPageDeletedObjects, contains( elementE ) );
    }

    @Test
    public void testSearchForKlass()
    {
        deletedObjectService.addDeletedObject( elementA );
        deletedObjectService.addDeletedObject( elementB );
        deletedObjectService.addDeletedObject( elementC );

        deletedObjectService.addDeletedObject( new DeletedObject( createTodotask( 'A' ) ) );
        deletedObjectService.addDeletedObject( new DeletedObject( createTodotask( 'B' ) ) );
        deletedObjectService.addDeletedObject( new DeletedObject( createTodotask( 'C' ) ) );

        assertEquals( 6, deletedObjectService.countDeletedObjects() );
        assertEquals( 3, deletedObjectService.getDeletedObjectsByKlass( "DataElement" ).size() );
        assertEquals( 3, deletedObjectService.getDeletedObjectsByKlass( "TodoTask" ).size() );
        assertTrue( deletedObjectService.getDeletedObjectsByKlass( "Indicator" ).isEmpty() );
    }

    @Test
    public void testDeleteDataElement()
    {
        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        DataElement dataElementC = createDataElement( 'C' );
        TodoTask todotaskA = createTodotask( 'A' );
        TodoTask todotaskB = createTodotask( 'B' );

        manager.save( dataElementA );
        manager.save( dataElementB );
        manager.save( dataElementC );
        manager.save( todotaskA );
        manager.save( todotaskB );

        manager.delete( dataElementA );
        manager.delete( dataElementB );
        manager.delete( dataElementC );
        manager.delete( todotaskA );
        manager.delete( todotaskB );

        assertEquals( 5, deletedObjectService.countDeletedObjects() );
        assertEquals( 3, deletedObjectService.getDeletedObjectsByKlass( "DataElement" ).size() );
        assertEquals( 2, deletedObjectService.getDeletedObjectsByKlass( "TodoTask" ).size() );
    }

    @Override
    public boolean emptyDatabaseAfterTest()
    {
        return true;
    }
}
