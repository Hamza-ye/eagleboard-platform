package com.mass3d.query.operators;

import java.util.Collection;
import java.util.Date;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import com.mass3d.query.QueryException;
import com.mass3d.query.QueryUtils;
import com.mass3d.query.Type;
import com.mass3d.query.Typed;
import com.mass3d.query.planner.QueryPath;
import com.mass3d.schema.Property;

public class EqualOperator extends Operator
{
    public EqualOperator( Object arg )
    {
        super( "eq", Typed.from( String.class, Boolean.class, Number.class, Date.class, Enum.class ), arg );
    }

    public EqualOperator( String name, Object arg )
    {
        super( name, Typed.from( String.class, Boolean.class, Number.class, Date.class, Enum.class ), arg );
    }

    @Override
    public Criterion getHibernateCriterion( QueryPath queryPath )
    {
        Property property = queryPath.getProperty();

        if ( property.isCollection() )
        {
            Integer value = QueryUtils.parseValue( Integer.class, args.get( 0 ) );

            if ( value == null )
            {
                throw new QueryException( "Left-side is collection, and right-side is not a valid integer, so can't compare by size." );
            }

            return Restrictions.sizeEq( queryPath.getPath(), value );
        }

        return Restrictions.eq( queryPath.getPath(), args.get( 0 ) );
    }

    @Override
    public boolean test( Object value )
    {
        if ( args.isEmpty() || value == null )
        {
            return false;
        }

        Type type = new Type( value );

        if ( type.isString() )
        {
            String s1 = getValue( String.class );
            String s2 = (String) value;

            return s1 != null && s2.equals( s1 );
        }
        else if ( type.isBoolean() )
        {
            Boolean s1 = getValue( Boolean.class );
            Boolean s2 = (Boolean) value;

            return s1 != null && s2.equals( s1 );
        }
        else if ( type.isInteger() )
        {
            Integer s1 = getValue( Integer.class );
            Integer s2 = (Integer) value;

            return s1 != null && s2.equals( s1 );
        }
        else if ( type.isFloat() )
        {
            Float s1 = getValue( Float.class );
            Float s2 = (Float) value;

            return s1 != null && s2.equals( s1 );
        }
        else if ( type.isCollection() )
        {
            Collection<?> collection = (Collection<?>) value;
            Integer size = getValue( Integer.class );

            return size != null && collection.size() == size;
        }
        else if ( type.isDate() )
        {
            Date s1 = getValue( Date.class );
            Date s2 = (Date) value;

            return s1 != null && s2.equals( s1 );
        }
        else if ( type.isEnum() )
        {
            String s1 = String.valueOf( args.get( 0 ) );
            String s2 = String.valueOf( value );

            return s2.equals( s1 );
        }

        return false;
    }
}
