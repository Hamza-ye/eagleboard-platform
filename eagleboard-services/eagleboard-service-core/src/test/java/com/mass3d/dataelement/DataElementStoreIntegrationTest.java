package com.mass3d.dataelement;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.mass3d.IntegrationTestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DataElementStoreIntegrationTest
    extends IntegrationTestBase
{
    @Autowired
    private DataElementStore dataElementStore;

    @Override
    public boolean emptyDatabaseAfterTest()
    {
        return true;
    }

    @Test
    public void testDeleteAndGetDataElement()
    {
        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        DataElement dataElementC = createDataElement( 'C' );
        DataElement dataElementD = createDataElement( 'D' );

        dataElementStore.save( dataElementA );
        long idA = dataElementA.getId();
        dataElementStore.save( dataElementB );
        long idB = dataElementB.getId();
        dataElementStore.save( dataElementC );
        long idC = dataElementC.getId();
        dataElementStore.save( dataElementD );
        long idD = dataElementD.getId();

        assertNotNull( dataElementStore.get( idA ) );
        assertNotNull( dataElementStore.get( idB ) );
        assertNotNull( dataElementStore.get( idC ) );
        assertNotNull( dataElementStore.get( idD ) );

        dataElementA = dataElementStore.get( idA );
        dataElementB = dataElementStore.get( idB );
        dataElementC = dataElementStore.get( idC );
        dataElementD = dataElementStore.get( idD );

        dataElementStore.delete( dataElementA );
        assertNull( dataElementStore.get( idA ) );
        assertNotNull( dataElementStore.get( idB ) );
        assertNotNull( dataElementStore.get( idC ) );
        assertNotNull( dataElementStore.get( idD ) );

        dataElementStore.delete( dataElementB );
        assertNull( dataElementStore.get( idA ) );
        assertNull( dataElementStore.get( idB ) );
        assertNotNull( dataElementStore.get( idC ) );
        assertNotNull( dataElementStore.get( idD ) );

        dataElementStore.delete( dataElementC );
        assertNull( dataElementStore.get( idA ) );
        assertNull( dataElementStore.get( idB ) );
        assertNull( dataElementStore.get( idC ) );
        assertNotNull( dataElementStore.get( idD ) );

        dataElementStore.delete( dataElementD );
        assertNull( dataElementStore.get( idA ) );
        assertNull( dataElementStore.get( idB ) );
        assertNull( dataElementStore.get( idC ) );
        assertNull( dataElementStore.get( idD ) );
    }

}
