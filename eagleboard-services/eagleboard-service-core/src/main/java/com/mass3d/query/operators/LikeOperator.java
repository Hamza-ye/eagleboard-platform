package com.mass3d.query.operators;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import com.mass3d.query.Type;
import com.mass3d.query.Typed;
import com.mass3d.query.planner.QueryPath;

public class LikeOperator extends Operator
{
    private final boolean caseSensitive;

    private final MatchMode matchMode;

    public LikeOperator( Object arg, boolean caseSensitive, com.mass3d.query.operators.MatchMode matchMode )
    {
        super( "like", Typed.from( String.class ), arg );
        this.caseSensitive = caseSensitive;
        this.matchMode = getMatchMode( matchMode );
    }

    public LikeOperator( String name, Object arg, boolean caseSensitive, com.mass3d.query.operators.MatchMode matchMode )
    {
        super( name, Typed.from( String.class ), arg );
        this.caseSensitive = caseSensitive;
        this.matchMode = getMatchMode( matchMode );
    }

    @Override
    public Criterion getHibernateCriterion( QueryPath queryPath )
    {
        if ( caseSensitive )
        {
            return Restrictions
                .like( queryPath.getPath(), String.valueOf( args.get( 0 ) ).replace( "%", "\\%" ), matchMode );
        }
        else
        {
            return Restrictions
                .ilike( queryPath.getPath(), String.valueOf( args.get( 0 ) ).replace( "%", "\\%" ), matchMode );
        }
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
            String s1 = caseSensitive ? getValue( String.class ) : getValue( String.class ).toLowerCase();
            String s2 = caseSensitive ? (String) value : ((String) value).toLowerCase();

            switch ( matchMode )
            {
                case EXACT:
                    return s2.equals( s1 );
                case START:
                    return s2.startsWith( s1 );
                case END:
                    return s2.endsWith( s1 );
                case ANYWHERE:
                    return s2.contains( s1 );
            }
        }

        return false;
    }
}
