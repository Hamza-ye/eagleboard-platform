package com.mass3d.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import com.mass3d.DhisSpringTest;
import com.mass3d.dataelement.DataElement;
import com.mass3d.query.operators.EqualOperator;
import com.mass3d.query.operators.NullOperator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class QueryParserTest
    extends DhisSpringTest
{
    @Autowired
    private QueryParser queryParser;

    @Test( expected = QueryParserException.class )
    public void failedFilters() throws QueryParserException
    {
        queryParser.parse( DataElement.class, Arrays.asList( "id", "name" ) );
    }

    @Test
    public void eqOperator() throws QueryParserException
    {
        Query query = queryParser.parse( DataElement.class, Arrays.asList( "id:eq:1", "id:eq:2" ) );
        assertEquals( 2, query.getCriterions().size() );

        Restriction restriction = (Restriction) query.getCriterions().get( 0 );
        assertEquals( "id", restriction.getPath() );
        assertEquals( "1", restriction.getOperator().getArgs().get( 0 ) );
        assertTrue( restriction.getOperator() instanceof EqualOperator );

        restriction = (Restriction) query.getCriterions().get( 1 );
        assertEquals( "id", restriction.getPath() );
        assertEquals( "2", restriction.getOperator().getArgs().get( 0 ) );
        assertTrue( restriction.getOperator() instanceof EqualOperator );
    }

    @Test
    public void eqOperatorDeepPath1() throws QueryParserException
    {
        Query query = queryParser.parse( DataElement.class, Arrays.asList( "dataElementGroups.id:eq:1", "dataElementGroups.id:eq:2" ) );
        assertEquals( 2, query.getCriterions().size() );

        Restriction restriction = (Restriction) query.getCriterions().get( 0 );
        assertEquals( "dataElementGroups.id", restriction.getPath() );
        assertEquals( "1", restriction.getOperator().getArgs().get( 0 ) );
        assertTrue( restriction.getOperator() instanceof EqualOperator );

        restriction = (Restriction) query.getCriterions().get( 1 );
        assertEquals( "dataElementGroups.id", restriction.getPath() );
        assertEquals( "2", restriction.getOperator().getArgs().get( 0 ) );
        assertTrue( restriction.getOperator() instanceof EqualOperator );
    }

    @Test( expected = QueryParserException.class )
    public void eqOperatorDeepPathFail() throws QueryParserException
    {
        queryParser.parse( DataElement.class, Arrays.asList( "dataElementGroups.id.name:eq:1", "dataElementGroups.id.abc:eq:2" ) );
    }

    @Test
    public void nullOperator() throws QueryParserException
    {
        Query query = queryParser.parse( DataElement.class, Arrays.asList( "id:null", "name:null" ) );
        assertEquals( 2, query.getCriterions().size() );

        Restriction restriction = (Restriction) query.getCriterions().get( 0 );
        assertEquals( "id", restriction.getPath() );
        assertTrue( restriction.getOperator() instanceof NullOperator );

        restriction = (Restriction) query.getCriterions().get( 1 );
        assertEquals( "name", restriction.getPath() );
        assertTrue( restriction.getOperator() instanceof NullOperator );
    }
}