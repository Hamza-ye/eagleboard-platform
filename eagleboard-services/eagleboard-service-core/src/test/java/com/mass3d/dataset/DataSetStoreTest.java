package com.mass3d.dataset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import com.mass3d.DhisSpringTest;
import com.mass3d.period.PeriodType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @version $Id: DataSetStoreTest.java 3451 2007-07-09 12:28:19Z torgeilo $
 */
public class DataSetStoreTest
    extends DhisSpringTest
{
    @Autowired
    private DataSetStore dataSetStore;

//    @Autowired
//    private DataEntryFormService dataEntryFormService;

    private PeriodType periodType;

    @Override
    public void setUpTest()
        throws Exception
    {
        periodType = PeriodType.getAvailablePeriodTypes().iterator().next();
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private void assertEq( char uniqueCharacter, DataSet dataSet )
    {
        assertEquals( "DataSet" + uniqueCharacter, dataSet.getName() );
        assertEquals( "DataSetShort" + uniqueCharacter, dataSet.getShortName() );
        assertEquals( periodType, dataSet.getPeriodType() );
    }
    
    // -------------------------------------------------------------------------
    // DataSet
    // -------------------------------------------------------------------------
    
    @Test
    public void testAddDataSet()
    {
        DataSet dataSetA = createDataSet( 'A', periodType );
        DataSet dataSetB = createDataSet( 'B', periodType );

        dataSetStore.save( dataSetA );
        long idA = dataSetA.getId();
        dataSetStore.save( dataSetB );
        long idB = dataSetB.getId();

        dataSetA = dataSetStore.get( idA );
        dataSetB = dataSetStore.get( idB );

        assertEquals( idA, dataSetA.getId() );
        assertEq( 'A', dataSetA );

        assertEquals( idB, dataSetB.getId() );
        assertEq( 'B', dataSetB );
    }

    @Test
    public void testUpdateDataSet()
    {
        DataSet dataSet = createDataSet( 'A', periodType );

        dataSetStore.save( dataSet );
        long id = dataSet.getId();

        dataSet = dataSetStore.get( id );

        assertEq( 'A', dataSet );

        dataSet.setName( "DataSetB" );

        dataSetStore.update( dataSet );

        dataSet = dataSetStore.get( id );

        assertEquals( dataSet.getName(), "DataSetB" );
    }

    @Test
    public void testDeleteAndGetDataSet()
    {
        DataSet dataSetA = createDataSet( 'A', periodType );
        DataSet dataSetB = createDataSet( 'B', periodType );

        dataSetStore.save( dataSetA );
        long idA = dataSetA.getId();
        dataSetStore.save( dataSetB );
        long idB = dataSetB.getId();

        assertNotNull( dataSetStore.get( idA ) );
        assertNotNull( dataSetStore.get( idB ) );

        dataSetStore.delete( dataSetStore.get( idA ) );

        assertNull( dataSetStore.get( idA ) );
        assertNotNull( dataSetStore.get( idB ) );
    }

    @Test
    public void testGetDataSetByName()
    {
        DataSet dataSetA = createDataSet( 'A', periodType );
        DataSet dataSetB = createDataSet( 'B', periodType );

        dataSetStore.save( dataSetA );
        long idA = dataSetA.getId();
        dataSetStore.save( dataSetB );
        long idB = dataSetB.getId();

        assertEquals( dataSetStore.getByName( "DataSetA" ).getId(), idA );
        assertEquals( dataSetStore.getByName( "DataSetB" ).getId(), idB );
        assertNull( dataSetStore.getByName( "DataSetC" ) );
    }

    @Test
    public void testGetAllDataSets()
    {
        DataSet dataSetA = createDataSet( 'A', periodType );
        DataSet dataSetB = createDataSet( 'B', periodType );

        dataSetStore.save( dataSetA );
        dataSetStore.save( dataSetB );

        List<DataSet> dataSets = dataSetStore.getAll();

        assertEquals( dataSets.size(), 2 );
        assertTrue( dataSets.contains( dataSetA ) );
        assertTrue( dataSets.contains( dataSetB ) );
    }

    @Test
    public void testGetDataSetByPeriodType()
    {
        List<PeriodType> types = PeriodType.getAvailablePeriodTypes();
        PeriodType periodType1 = types.get( 0 );
        PeriodType periodType2 = types.get( 1 );
        DataSet dataSetA = createDataSet( 'A', periodType1 );
        DataSet dataSetB = createDataSet( 'B', periodType2 );

        dataSetStore.save( dataSetA );
        dataSetStore.save( dataSetB );

        assertEquals( 1, dataSetStore.getDataSetsByPeriodType( periodType1 ).size() );
        assertEquals( 1, dataSetStore.getDataSetsByPeriodType( periodType2 ).size() );
    }

//    @Test
//    public void testGetByDataEntryForm()
//    {
//        DataEntryForm dataEntryFormX = createDataEntryForm( 'X' );
//        DataEntryForm dataEntryFormY = createDataEntryForm( 'Y' );
//
//        dataEntryFormService.addDataEntryForm( dataEntryFormX );
//        dataEntryFormService.addDataEntryForm( dataEntryFormY );
//
//        DataSet dataSetA = createDataSet( 'A', periodType );
//        DataSet dataSetB = createDataSet( 'B', periodType );
//        DataSet dataSetC = createDataSet( 'C', periodType );
//
//        dataSetA.setDataEntryForm( dataEntryFormX );
//
//        dataSetStore.save( dataSetA );
//        dataSetStore.save( dataSetB );
//        dataSetStore.save( dataSetC );
//
//        List<DataSet> dataSetsWithForm = dataSetStore.getDataSetsByDataEntryForm( dataEntryFormX );
//
//        assertEquals( 1, dataSetsWithForm.size() );
//        assertEquals( dataSetA, dataSetsWithForm.get( 0 ) );
//
//        dataSetC.setDataEntryForm( dataEntryFormX );
//
//        dataSetStore.update( dataSetC );
//
//        dataSetsWithForm = dataSetStore.getDataSetsByDataEntryForm( dataEntryFormX );
//
//        assertEquals( 2, dataSetsWithForm.size() );
//        assertTrue( dataSetsWithForm.contains( dataSetA ) );
//        assertTrue( dataSetsWithForm.contains( dataSetC ) );
//
//        dataSetB.setDataEntryForm( dataEntryFormY );
//        dataSetStore.update( dataSetB );
//
//        dataSetsWithForm = dataSetStore.getDataSetsByDataEntryForm( dataEntryFormY );
//        assertEquals( 1, dataSetsWithForm.size() );
//        assertTrue( dataSetsWithForm.contains( dataSetB ) );
//    }
}
