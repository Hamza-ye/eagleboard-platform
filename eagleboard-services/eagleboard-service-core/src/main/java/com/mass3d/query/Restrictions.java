package com.mass3d.query;

import java.util.Collection;
import com.mass3d.query.operators.BetweenOperator;
import com.mass3d.query.operators.EmptyOperator;
import com.mass3d.query.operators.EqualOperator;
import com.mass3d.query.operators.GreaterEqualOperator;
import com.mass3d.query.operators.GreaterThanOperator;
import com.mass3d.query.operators.InOperator;
import com.mass3d.query.operators.LessEqualOperator;
import com.mass3d.query.operators.LessThanOperator;
import com.mass3d.query.operators.LikeOperator;
import com.mass3d.query.operators.MatchMode;
import com.mass3d.query.operators.NotEqualOperator;
import com.mass3d.query.operators.NotInOperator;
import com.mass3d.query.operators.NotLikeOperator;
import com.mass3d.query.operators.NotNullOperator;
import com.mass3d.query.operators.NotTokenOperator;
import com.mass3d.query.operators.NullOperator;
import com.mass3d.query.operators.TokenOperator;

public final class Restrictions
{
    public static Restriction eq( String path, Object value )
    {
        return new Restriction( path, new EqualOperator( value ) );
    }

    public static Restriction ne( String path, Object value )
    {
        return new Restriction( path, new NotEqualOperator( value ) );
    }

    public static Restriction gt( String path, Object value )
    {
        return new Restriction( path, new GreaterThanOperator( value ) );
    }

    public static Restriction lt( String path, Object value )
    {
        return new Restriction( path, new LessThanOperator( value ) );
    }

    public static Restriction ge( String path, Object value )
    {
        return new Restriction( path, new GreaterEqualOperator( value ) );
    }

    public static Restriction le( String path, Object value )
    {
        return new Restriction( path, new LessEqualOperator( value ) );
    }

    public static Restriction between( String path, Object lside, Object rside )
    {
        return new Restriction( path, new BetweenOperator( lside, rside ) );
    }

    public static Restriction like( String path, Object value, MatchMode matchMode )
    {
        return new Restriction( path, new LikeOperator( value, true, matchMode ) );
    }

    public static Restriction notLike( String path, Object value, MatchMode matchMode )
    {
        return new Restriction( path, new NotLikeOperator( value, true, matchMode ) );
    }

    public static Restriction ilike( String path, Object value, MatchMode matchMode )
    {
        return new Restriction( path, new LikeOperator( value, false, matchMode ) );
    }

    public static Restriction notIlike( String path, Object value, MatchMode matchMode )
    {
        return new Restriction( path, new NotLikeOperator( value, false, matchMode ) );
    }

    public static Restriction token( String path, Object value, MatchMode matchMode )
    {
        return new Restriction( path, new TokenOperator( value, false, matchMode ) );
    }

    public static Restriction notToken( String path, Object value, MatchMode matchMode )
    {
        return new Restriction( path, new NotTokenOperator( value, false, matchMode ) );
    }

    public static Restriction in( String path, Collection<?> values )
    {
        return new Restriction( path, new InOperator( values ) );
    }

    public static Restriction notIn( String path, Collection<?> values )
    {
        return new Restriction( path, new NotInOperator( values ) );
    }

    public static Restriction isNull( String path )
    {
        return new Restriction( path, new NullOperator() );
    }

    public static Restriction isNotNull( String path )
    {
        return new Restriction( path, new NotNullOperator() );
    }

    public static Restriction isEmpty( String path )
    {
        return new Restriction( path, new EmptyOperator() );
    }

    private Restrictions()
    {
    }
}
