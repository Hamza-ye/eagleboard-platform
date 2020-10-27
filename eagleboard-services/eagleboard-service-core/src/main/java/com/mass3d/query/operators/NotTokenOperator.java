package com.mass3d.query.operators;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import com.mass3d.query.Typed;
import com.mass3d.query.planner.QueryPath;

public class NotTokenOperator
    extends Operator
{
    private final boolean caseSensitive;

    private final org.hibernate.criterion.MatchMode matchMode;

    public NotTokenOperator( Object arg, boolean caseSensitive, com.mass3d.query.operators.MatchMode matchMode )
    {
        super( "!token", Typed.from( String.class ), arg );
        this.caseSensitive = caseSensitive;
        this.matchMode = getMatchMode( matchMode );
    }

    @Override
    public Criterion getHibernateCriterion( QueryPath queryPath )
    {
        String value = caseSensitive ? getValue( String.class ) : getValue( String.class ).toLowerCase();

        return Restrictions.sqlRestriction( "c_." + queryPath.getPath() + " !~* '" + TokenUtils.createRegex( value ) + "' " );
    }

    @Override
    public boolean test( Object value )
    {
        String targetValue = caseSensitive ? getValue( String.class ) : getValue( String.class ).toLowerCase();
        return !TokenUtils.test( args, value, targetValue, caseSensitive, matchMode );
    }
}