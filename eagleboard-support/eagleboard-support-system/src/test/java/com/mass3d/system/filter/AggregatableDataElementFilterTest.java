package com.mass3d.system.filter;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.Sets;
import java.util.Set;
import com.mass3d.DhisSpringTest;
import com.mass3d.common.ValueType;
import com.mass3d.commons.filter.FilterUtils;
import com.mass3d.dataelement.DataElement;
import org.junit.Test;

public class AggregatableDataElementFilterTest
    extends DhisSpringTest
{
    @Test
    public void filter()
    {
        DataElement elA = createDataElement( 'A' );
        DataElement elB = createDataElement( 'B' );
        DataElement elC = createDataElement( 'C' );
        DataElement elD = createDataElement( 'D' );
        DataElement elE = createDataElement( 'E' );
        DataElement elF = createDataElement( 'F' );

        elA.setValueType( ValueType.BOOLEAN );
        elB.setValueType( ValueType.INTEGER );
        elC.setValueType( ValueType.DATE );
        elD.setValueType( ValueType.BOOLEAN );
        elE.setValueType( ValueType.INTEGER );
        elF.setValueType( ValueType.DATE );

        Set<DataElement> set = Sets.newHashSet( elA, elB, elC, elD, elE, elF );

        Set<DataElement> reference = Sets.newHashSet( elA, elB, elD, elE );

        FilterUtils.filter( set, AggregatableDataElementFilter.INSTANCE );

        assertEquals( reference.size(), set.size() );
        assertEquals( reference, set );

        set = Sets.newHashSet( elA, elB, elC, elD, elE, elF );

        Set<DataElement> inverseReference = Sets.newHashSet( elC, elF );

        FilterUtils.inverseFilter( set, AggregatableDataElementFilter.INSTANCE );

        assertEquals( inverseReference.size(), set.size() );
        assertEquals( inverseReference, set );
    }
}
