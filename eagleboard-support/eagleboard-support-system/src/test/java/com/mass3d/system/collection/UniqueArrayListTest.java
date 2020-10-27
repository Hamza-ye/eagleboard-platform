package com.mass3d.system.collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import com.mass3d.commons.collection.UniqueArrayList;
import com.mass3d.period.MonthlyPeriodType;
import com.mass3d.period.Period;
import com.mass3d.period.PeriodType;
import org.joda.time.DateTime;
import org.junit.Test;

public class UniqueArrayListTest
{
    @Test
    public void testAdd()
    {
        List<Period> list = new UniqueArrayList<>();
        
        PeriodType periodType = new MonthlyPeriodType();
        Period peA = periodType.createPeriod( new DateTime( 2000, 1, 1, 0, 0 ).toDate() );
        Period peB = periodType.createPeriod( new DateTime( 2000, 1, 1, 0, 0 ).toDate() ); // Duplicate
        Period peC = periodType.createPeriod( new DateTime( 2000, 2, 1, 0, 0 ).toDate() );
        
        list.add( peA );
        list.add( peB );
        list.add( peC );
        
        assertEquals( 2, list.size() );
        assertTrue( list.contains( peA ) );
        assertTrue( list.contains( peB ) );
        assertTrue( list.contains( peC ) );
    }

    @Test
    public void testAddAll()
    {
        List<Period> list = new ArrayList<>();
        
        PeriodType periodType = new MonthlyPeriodType();
        Period peA = periodType.createPeriod( new DateTime( 2000, 1, 1, 0, 0 ).toDate() );
        Period peB = periodType.createPeriod( new DateTime( 2000, 1, 1, 0, 0 ).toDate() ); // Duplicate
        Period peC = periodType.createPeriod( new DateTime( 2000, 2, 1, 0, 0 ).toDate() );
        
        list.add( peA );
        list.add( peB );
        list.add( peC );
        
        assertEquals( 3, list.size() );
        
        List<Period> uniqueList = new UniqueArrayList<>();
        uniqueList.addAll( list );
        
        assertEquals( 2, uniqueList.size() );
        assertTrue( uniqueList.contains( peA ) );
        assertTrue( uniqueList.contains( peB ) );
        assertTrue( uniqueList.contains( peC ) );
    }
    
    @Test
    public void testCollectionConstructor()
    {
        List<Period> list = new ArrayList<>();
        
        PeriodType periodType = new MonthlyPeriodType();
        Period peA = periodType.createPeriod( new DateTime( 2000, 1, 1, 0, 0 ).toDate() );
        Period peB = periodType.createPeriod( new DateTime( 2000, 1, 1, 0, 0 ).toDate() ); // Duplicate
        Period peC = periodType.createPeriod( new DateTime( 2000, 2, 1, 0, 0 ).toDate() );
        
        list.add( peA );
        list.add( peB );
        list.add( peC );
        
        assertEquals( 3, list.size() );
        
        List<Period> uniqueList = new UniqueArrayList<>( list );
        
        assertEquals( 2, uniqueList.size() );
        assertTrue( uniqueList.contains( peA ) );
        assertTrue( uniqueList.contains( peB ) );
        assertTrue( uniqueList.contains( peC ) );
    }
}
