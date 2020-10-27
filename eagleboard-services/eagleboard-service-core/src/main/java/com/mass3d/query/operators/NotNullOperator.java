package com.mass3d.query.operators;

import java.util.Date;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import com.mass3d.query.Typed;
import com.mass3d.query.planner.QueryPath;

public class NotNullOperator extends Operator
{
    public NotNullOperator()
    {
        super( "!null", Typed.from( String.class, Boolean.class, Number.class, Date.class, Enum.class ) );
    }

    @Override
    public Criterion getHibernateCriterion( QueryPath queryPath )
    {
        return Restrictions.isNotNull( queryPath.getPath() );
    }

    @Override
    public boolean test( Object value )
    {
        return value != null;
    }
}
