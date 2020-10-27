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

public class LessEqualOperator extends Operator
{
    public LessEqualOperator( Object arg )
    {
        super( "le", Typed.from( String.class, Boolean.class, Number.class, Date.class ), arg );
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

            return Restrictions.sizeLe( queryPath.getPath(), value );
        }

        return Restrictions.le( queryPath.getPath(), args.get( 0 ) );
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

            return s1 != null && (s2.equals( s1 ) || s2.compareTo( s1 ) < 0);
        }
        else if ( type.isInteger() )
        {
            Integer s1 = getValue( Integer.class );
            Integer s2 = (Integer) value;

            return s1 != null && s2 <= s1;
        }
        else if ( type.isFloat() )
        {
            Float s1 = getValue( Float.class );
            Float s2 = (Float) value;

            return s1 != null && s2 <= s1;
        }
        else if ( type.isDate() )
        {
            Date s1 = getValue( Date.class );
            Date s2 = (Date) value;

            return s1 != null && (s2.before( s1 ) || s2.equals( s1 ));
        }
        else if ( type.isCollection() )
        {
            Collection<?> collection = (Collection<?>) value;
            Integer size = getValue( Integer.class );

            return size != null && collection.size() <= size;
        }

        return false;
    }
}
