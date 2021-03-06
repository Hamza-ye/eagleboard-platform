package com.mass3d.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.mass3d.dataelement.DataElementGroup;
import java.util.Collection;
import java.util.List;
import com.mass3d.DhisSpringTest;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.common.ValueType;
import com.mass3d.dataelement.DataElement;
import com.mass3d.query.operators.MatchMode;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaService;
import org.jfree.data.time.Year;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CriteriaQueryEngineTest
    extends DhisSpringTest
{
    @Autowired
    private SchemaService schemaService;

    @Autowired
    private QueryService queryService;

    @Autowired
    private CriteriaQueryEngine<? extends IdentifiableObject> queryEngine;

    @Autowired
    private IdentifiableObjectManager identifiableObjectManager;

    @Before
    public void createDataElements()
    {
        DataElement dataElementA = createDataElement( 'A' );
        dataElementA.setValueType( ValueType.NUMBER );
        dataElementA.setDisplayName( "dataElementA" );
        dataElementA.setName( "dataElementA" );

        DataElement dataElementB = createDataElement( 'B' );
        dataElementB.setValueType( ValueType.BOOLEAN );
        dataElementB.setDisplayName( "dataElementB" );
        dataElementB.setName( "dataElementB" );

        DataElement dataElementC = createDataElement( 'C' );
        dataElementC.setValueType( ValueType.INTEGER );
        dataElementC.setDisplayName( "dataElementC" );
        dataElementC.setName( "dataElementC" );

        DataElement dataElementD = createDataElement( 'D' );
        dataElementD.setValueType( ValueType.NUMBER );
        dataElementD.setDisplayName( "dataElementD" );
        dataElementD.setName( "dataElementD" );

        DataElement dataElementE = createDataElement( 'E' );
        dataElementE.setValueType( ValueType.BOOLEAN );
        dataElementE.setDisplayName( "dataElementE" );
        dataElementE.setName( "dataElementE" );

        DataElement dataElementF = createDataElement( 'F' );
        dataElementF.setValueType( ValueType.INTEGER );
        dataElementF.setDisplayName( "dataElementF" );
        dataElementF.setName( "dataElementF" );

        dataElementA.setCreated( Year.parseYear( "2001" ).getStart() );
        dataElementB.setCreated( Year.parseYear( "2002" ).getStart() );
        dataElementC.setCreated( Year.parseYear( "2003" ).getStart() );
        dataElementD.setCreated( Year.parseYear( "2004" ).getStart() );
        dataElementE.setCreated( Year.parseYear( "2005" ).getStart() );
        dataElementF.setCreated( Year.parseYear( "2006" ).getStart() );

        identifiableObjectManager.save( dataElementB );
        identifiableObjectManager.save( dataElementE );
        identifiableObjectManager.save( dataElementA );
        identifiableObjectManager.save( dataElementC );
        identifiableObjectManager.save( dataElementF );
        identifiableObjectManager.save( dataElementD );

        DataElementGroup dataElementGroupA = createDataElementGroup( 'A' );
        dataElementGroupA.addDataElement( dataElementA );
        dataElementGroupA.addDataElement( dataElementB );
        dataElementGroupA.addDataElement( dataElementC );
        dataElementGroupA.addDataElement( dataElementD );

        DataElementGroup dataElementGroupB = createDataElementGroup( 'B' );
        dataElementGroupB.addDataElement( dataElementE );
        dataElementGroupB.addDataElement( dataElementF );

        identifiableObjectManager.save( dataElementGroupA );
        identifiableObjectManager.save( dataElementGroupB );
    }

    private boolean collectionContainsUid( Collection<? extends IdentifiableObject> collection, String uid )
    {
        for ( IdentifiableObject identifiableObject : collection )
        {
            if ( identifiableObject.getUid().equals( uid ) )
            {
                return true;
            }
        }

        return false;
    }

    @Test
    public void getAllQuery()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        assertEquals( 6, queryEngine.query( query ).size() );
    }

    @Test
    public void getMinMaxQuery()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.setFirstResult( 2 );
        query.setMaxResults( 10 );

        assertEquals( 4, queryEngine.query( query ).size() );

        query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.setFirstResult( 2 );
        query.setMaxResults( 2 );

        assertEquals( 2, queryEngine.query( query ).size() );
    }

    @Test
    public void getEqQuery()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.add( Restrictions.eq( "id", "deabcdefghA" ) );
        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 1, objects.size() );
        assertEquals( "deabcdefghA", objects.get( 0 ).getUid() );
    }

    @Test
    public void getNeQuery()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.add( Restrictions.ne( "id", "deabcdefghA" ) );
        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 5, objects.size() );

        assertFalse( collectionContainsUid( objects, "deabcdefghA" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghB" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghC" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghD" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghE" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghF" ) );
    }

    @Test
    public void getLikeQuery()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.add( Restrictions.like( "name", "F", MatchMode.ANYWHERE ) );
        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 1, objects.size() );
        assertEquals( "deabcdefghF", objects.get( 0 ).getUid() );
    }

    @Test
    public void getGtQuery()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.add( Restrictions.gt( "created", Year.parseYear( "2003" ).getStart() ) );
        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 3, objects.size() );

        assertTrue( collectionContainsUid( objects, "deabcdefghD" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghE" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghF" ) );
    }

    @Test
    public void getLtQuery()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.add( Restrictions.lt( "created", Year.parseYear( "2003" ).getStart() ) );
        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 2, objects.size() );

        assertTrue( collectionContainsUid( objects, "deabcdefghA" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghB" ) );
    }

    @Test
    public void getGeQuery()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.add( Restrictions.ge( "created", Year.parseYear( "2003" ).getStart() ) );
        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 4, objects.size() );

        assertTrue( collectionContainsUid( objects, "deabcdefghC" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghD" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghE" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghF" ) );
    }

    @Test
    public void getLeQuery()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.add( Restrictions.le( "created", Year.parseYear( "2003" ).getStart() ) );
        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 3, objects.size() );

        assertTrue( collectionContainsUid( objects, "deabcdefghA" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghB" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghC" ) );
    }

    @Test
    public void getBetweenQuery()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.add( Restrictions.between( "created", Year.parseYear( "2003" ).getStart(), Year.parseYear( "2005" ).getStart() ) );
        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 3, objects.size() );

        assertTrue( collectionContainsUid( objects, "deabcdefghC" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghD" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghE" ) );
    }

    @Test
    public void testDateRange()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );

        query.add( Restrictions.ge( "created", Year.parseYear( "2002" ).getStart() ) );
        query.add( Restrictions.le( "created", Year.parseYear( "2004" ).getStart() ) );

        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 3, objects.size() );

        assertTrue( collectionContainsUid( objects, "deabcdefghB" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghC" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghD" ) );
    }

    @Test
    public void getInQuery()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.add( Restrictions.in( "id", Lists.newArrayList( "deabcdefghD", "deabcdefghF" ) ) );
        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 2, objects.size() );

        assertTrue( collectionContainsUid( objects, "deabcdefghD" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghF" ) );
    }

    @Test
    public void sortNameDesc()
    {
        Schema schema = schemaService.getDynamicSchema( DataElement.class );

        Query query = Query.from( schema );
        query.addOrder( new Order( schema.getProperty( "name" ), Direction.DESCENDING ) );
        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 6, objects.size() );

        assertEquals( "deabcdefghF", objects.get( 0 ).getUid() );
        assertEquals( "deabcdefghE", objects.get( 1 ).getUid() );
        assertEquals( "deabcdefghD", objects.get( 2 ).getUid() );
        assertEquals( "deabcdefghC", objects.get( 3 ).getUid() );
        assertEquals( "deabcdefghB", objects.get( 4 ).getUid() );
        assertEquals( "deabcdefghA", objects.get( 5 ).getUid() );
    }

    @Test
    public void sortNameAsc()
    {
        Schema schema = schemaService.getDynamicSchema( DataElement.class );

        Query query = Query.from( schema );
        query.addOrder( new Order( schema.getProperty( "name" ), Direction.ASCENDING ) );
        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 6, objects.size() );

        assertEquals( "deabcdefghA", objects.get( 0 ).getUid() );
        assertEquals( "deabcdefghB", objects.get( 1 ).getUid() );
        assertEquals( "deabcdefghC", objects.get( 2 ).getUid() );
        assertEquals( "deabcdefghD", objects.get( 3 ).getUid() );
        assertEquals( "deabcdefghE", objects.get( 4 ).getUid() );
        assertEquals( "deabcdefghF", objects.get( 5 ).getUid() );
    }

    @Test
    public void sortCreatedDesc()
    {
        Schema schema = schemaService.getDynamicSchema( DataElement.class );

        Query query = Query.from( schema );
        query.addOrder( new Order( schema.getProperty( "created" ), Direction.DESCENDING ) );
        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 6, objects.size() );

        assertEquals( "deabcdefghF", objects.get( 0 ).getUid() );
        assertEquals( "deabcdefghE", objects.get( 1 ).getUid() );
        assertEquals( "deabcdefghD", objects.get( 2 ).getUid() );
        assertEquals( "deabcdefghC", objects.get( 3 ).getUid() );
        assertEquals( "deabcdefghB", objects.get( 4 ).getUid() );
        assertEquals( "deabcdefghA", objects.get( 5 ).getUid() );
    }

    @Test
    public void sortCreatedAsc()
    {
        Schema schema = schemaService.getDynamicSchema( DataElement.class );

        Query query = Query.from( schema );
        query.addOrder( new Order( schema.getProperty( "created" ), Direction.ASCENDING ) );
        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 6, objects.size() );

        assertEquals( "deabcdefghA", objects.get( 0 ).getUid() );
        assertEquals( "deabcdefghB", objects.get( 1 ).getUid() );
        assertEquals( "deabcdefghC", objects.get( 2 ).getUid() );
        assertEquals( "deabcdefghD", objects.get( 3 ).getUid() );
        assertEquals( "deabcdefghE", objects.get( 4 ).getUid() );
        assertEquals( "deabcdefghF", objects.get( 5 ).getUid() );
    }

    @Test
    public void testDoubleEqConjunction()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );

        Conjunction conjunction = query.conjunction();
        conjunction.add( Restrictions.eq( "id", "deabcdefghD" ) );
        conjunction.add( Restrictions.eq( "id", "deabcdefghF" ) );
        query.add( conjunction );

        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 0, objects.size() );
    }

    @Test
    public void testDoubleEqDisjunction()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );

        Disjunction disjunction = query.disjunction();
        disjunction.add( Restrictions.eq( "id", "deabcdefghD" ) );
        disjunction.add( Restrictions.eq( "id", "deabcdefghF" ) );
        query.add( disjunction );

        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 2, objects.size() );

        assertTrue( collectionContainsUid( objects, "deabcdefghD" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghF" ) );
    }

    @Test
    public void testDateRangeWithConjunction()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );

        Conjunction conjunction = query.conjunction();
        conjunction.add( Restrictions.ge( "created", Year.parseYear( "2002" ).getStart() ) );
        conjunction.add( Restrictions.le( "created", Year.parseYear( "2004" ).getStart() ) );
        query.add( conjunction );

        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 3, objects.size() );

        assertTrue( collectionContainsUid( objects, "deabcdefghB" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghC" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghD" ) );
    }

    @Test
    @Ignore
    public void testIsNull()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.add( Restrictions.isNull( "categoryCombo" ) );

        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 0, objects.size() );
    }

    @Test
    @Ignore
    public void testIsNotNull()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ) );
        query.add( Restrictions.isNotNull( "categoryCombo" ) );

        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 6, objects.size() );

        assertTrue( collectionContainsUid( objects, "deabcdefghA" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghB" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghC" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghD" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghE" ) );
        assertTrue( collectionContainsUid( objects, "deabcdefghF" ) );
    }

    @Test
    @Ignore
    public void testCollectionEqSize4()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElementGroup.class ) );
        query.add( Restrictions.eq( "dataElements", 4 ) );
        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 1, objects.size() );
        assertEquals( "abcdefghijA", objects.get( 0 ).getUid() );
    }

    @Test
    @Ignore
    public void testCollectionEqSize2()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElementGroup.class ) );
        query.add( Restrictions.eq( "dataElements", 2 ) );
        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 1, objects.size() );
        assertEquals( "abcdefghijB", objects.get( 0 ).getUid() );
    }

    @Test
    public void testIdentifiableSearch1()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElementGroup.class ), Junction.Type.OR );
        query.add( Restrictions.eq( "name", "DataElementGroupA" ) );
        query.add( Restrictions.eq( "name", "DataElementGroupB" ) );

        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 2, objects.size() );
    }

    @Test
    public void testIdentifiableSearch2()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElementGroup.class ), Junction.Type.OR );

        Junction disjunction = new Disjunction( schemaService.getDynamicSchema( DataElementGroup.class ) );
        disjunction.add( Restrictions.eq( "name", "DataElementGroupA" ) );
        disjunction.add( Restrictions.eq( "name", "DataElementGroupB" ) );
        query.add( disjunction );

        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 2, objects.size() );
    }

    @Test
    public void testIdentifiableSearch3()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElementGroup.class ) );

        Junction disjunction = new Disjunction( schemaService.getDynamicSchema( DataElementGroup.class ) );
        disjunction.add( Restrictions.like( "name", "GroupA", MatchMode.ANYWHERE ) );
        query.add( disjunction );

        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 1, objects.size() );
    }

    @Test
    public void testIdentifiableSearch4()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElementGroup.class ), Junction.Type.OR );

        Junction disjunction = new Disjunction( schemaService.getDynamicSchema( DataElementGroup.class ) );
        disjunction.add( Restrictions.like( "name", "GroupA", MatchMode.ANYWHERE ) );
        disjunction.add( Restrictions.like( "name", "GroupA", MatchMode.ANYWHERE ) );
        query.add( disjunction );

        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 1, objects.size() );
    }

    @Test
    public void testIdentifiableSearch5()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElementGroup.class ), Junction.Type.OR );

        Junction disjunction = new Disjunction( schemaService.getDynamicSchema( DataElementGroup.class ) );
        disjunction.add( Restrictions.like( "name", "GroupA", MatchMode.ANYWHERE ) );
        disjunction.add( Restrictions.like( "name", "GroupA", MatchMode.ANYWHERE ) );
        disjunction.add( Restrictions.like( "name", "GroupB", MatchMode.ANYWHERE ) );
        query.add( disjunction );

        List<? extends IdentifiableObject> objects = queryEngine.query( query );

        assertEquals( 2, objects.size() );
    }

    @Test
    public void testIdentifiableSearch6()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ), Junction.Type.OR );

        Restriction nameRestriction = Restrictions.like( "name", "deF", MatchMode.ANYWHERE );
        Restriction uidRestriction = Restrictions.like( "id", "deF", MatchMode.ANYWHERE );
        Restriction codeRestriction = Restrictions.like( "code", "deF", MatchMode.ANYWHERE );

        Junction identifiableJunction = new Disjunction( schemaService.getDynamicSchema( DataElement.class ) );
        identifiableJunction.add( nameRestriction );
        identifiableJunction.add( uidRestriction );
        identifiableJunction.add( codeRestriction );

        query.add( identifiableJunction );

        List<? extends IdentifiableObject> objects = queryEngine.query( query );
        assertEquals( 1, objects.size() );
    }

    @Test
    @Ignore
    public void testIdentifiableSearch7()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ), Junction.Type.OR );

        Restriction nameRestriction = Restrictions.like( "name", "dataElement", MatchMode.ANYWHERE );
        Restriction uidRestriction = Restrictions.like( "id", "dataElement", MatchMode.ANYWHERE );
        Restriction codeRestriction = Restrictions.like( "code", "dataElement", MatchMode.ANYWHERE );

        Junction identifiableJunction = new Disjunction( schemaService.getDynamicSchema( DataElement.class ) );
        identifiableJunction.add( nameRestriction );
        identifiableJunction.add( uidRestriction );
        identifiableJunction.add( codeRestriction );

        query.add( identifiableJunction );

        List<? extends IdentifiableObject> objects = queryService.query( query );
        assertEquals( 6, objects.size() );
    }

    @Test
    @Ignore
    public void testIdentifiableSearch8()
    {
        Query query = Query.from( schemaService.getDynamicSchema( DataElement.class ), Junction.Type.OR );

        Restriction displayNameRestriction = Restrictions.like( "displayName", "dataElement", MatchMode.ANYWHERE );
        Restriction uidRestriction = Restrictions.like( "id", "dataElement", MatchMode.ANYWHERE );
        Restriction codeRestriction = Restrictions.like( "code", "dataElement", MatchMode.ANYWHERE );

        Junction identifiableJunction = new Disjunction( schemaService.getDynamicSchema( DataElement.class ) );
        identifiableJunction.add( displayNameRestriction );
        identifiableJunction.add( uidRestriction );
        identifiableJunction.add( codeRestriction );

        query.add( identifiableJunction );

        List<? extends IdentifiableObject> objects = queryService.query( query );
        assertEquals( 6, objects.size() );
    }
}
