package com.mass3d.query.operators;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import com.mass3d.query.planner.QueryPath;

public class NotEqualOperator extends EqualOperator
{
    public NotEqualOperator( Object arg )
    {
        super( "ne", arg );
    }

    @Override
    public Criterion getHibernateCriterion( QueryPath queryPath )
    {
        return Restrictions.not( super.getHibernateCriterion( queryPath ) );
    }

    @Override
    public boolean test( Object value )
    {
        return !super.test( value );
    }
}
