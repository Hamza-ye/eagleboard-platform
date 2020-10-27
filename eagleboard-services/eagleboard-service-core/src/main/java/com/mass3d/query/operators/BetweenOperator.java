package com.mass3d.query.operators;

import java.util.Collection;
import java.util.Date;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import com.mass3d.query.Type;
import com.mass3d.query.Typed;
import com.mass3d.query.planner.QueryPath;

public class BetweenOperator extends Operator
{
    public BetweenOperator( Object arg0, Object arg1 )
    {
        super( "between", Typed.from( String.class, Number.class, Date.class ), arg0, arg1 );
    }

    @Override
    public Criterion getHibernateCriterion( QueryPath queryPath )
    {
        return Restrictions.between( queryPath.getPath(), args.get( 0 ), args.get( 1 ) );
    }

    @Override
    public boolean test( Object value )
    {
        if ( args.isEmpty() || value == null )
        {
            return false;
        }

        Type type = new Type( value );

        if ( type.isInteger() )
        {
            Integer s1 = getValue( Integer.class, value );
            Integer min = getValue( Integer.class, 0 );
            Integer max = getValue( Integer.class, 1 );

            return s1 >= min && s1 <= max;
        }
        else if ( type.isFloat() )
        {
            Float s1 = getValue( Float.class, value );
            Integer min = getValue( Integer.class, 0 );
            Integer max = getValue( Integer.class, 1 );

            return s1 >= min && s1 <= max;
        }
        else if ( type.isDate() )
        {
            Date min = getValue( Date.class, 0 );
            Date max = getValue( Date.class, 1 );
            Date s2 = (Date) value;

            return (s2.equals( min ) || s2.after( min )) && (s2.before( max ) || s2.equals( max ));
        }
        else if ( type.isCollection() )
        {
            Collection<?> collection = (Collection<?>) value;
            Integer min = getValue( Integer.class, 0 );
            Integer max = getValue( Integer.class, 1 );

            return collection.size() >= min && collection.size() <= max;
        }

        return false;
    }
}
