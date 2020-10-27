package com.mass3d.query.operators;

import java.util.Collection;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import com.mass3d.query.Type;
import com.mass3d.query.Typed;
import com.mass3d.query.planner.QueryPath;

public class EmptyOperator extends Operator
{
    public EmptyOperator()
    {
        super( "empty", Typed.from( Collection.class ) );
    }

    @Override
    public Criterion getHibernateCriterion( QueryPath queryPath )
    {
        return Restrictions.sizeEq( queryPath.getPath(),0 );
    }

    @Override
    public boolean test( Object value )
    {
        if ( value == null )
        {
            return false;
        }

        Type type = new Type( value );

        if ( type.isCollection() )
        {
            Collection<?> collection = (Collection<?>) value;
            return collection.isEmpty();
        }

        return false;
    }
}