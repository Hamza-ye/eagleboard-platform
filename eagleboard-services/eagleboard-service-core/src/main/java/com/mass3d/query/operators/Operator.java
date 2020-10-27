package com.mass3d.query.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.hibernate.criterion.Criterion;
import com.mass3d.query.QueryParserException;
import com.mass3d.query.QueryUtils;
import com.mass3d.query.Type;
import com.mass3d.query.Typed;
import com.mass3d.query.planner.QueryPath;

public abstract class Operator
{
    protected final String name;

    protected final List<Object> args = new ArrayList<>();

    protected final Typed typed;

    protected Type argumentType;

    public Operator( String name, Typed typed )
    {
        this.name = name;
        this.typed = typed;
    }

    public Operator( String name, Typed typed, Object arg )
    {
        this( name, typed );
        this.argumentType = new Type( arg );
        this.args.add( arg );
        validate();
    }

    private void validate()
    {
        for ( Object arg : args )
        {
            if ( !isValid( arg.getClass() ) )
            {
                throw new QueryParserException( "Value `" + arg + "` of type `" + arg.getClass().getSimpleName() + "` is not supported by this operator." );
            }
        }
    }

    public Operator( String name, Typed typed, Object... args )
    {
        this( name, typed );
        this.argumentType = new Type( args[0] );
        Collections.addAll( this.args, args );
    }

    public List<Object> getArgs()
    {
        return args;
    }

    protected <T> T getValue( Class<T> klass, Class<?> secondaryClass, int idx )
    {
        return QueryUtils.parseValue( klass, secondaryClass, args.get( idx ) );
    }

    protected <T> T getValue( Class<T> klass, int idx )
    {
        return QueryUtils.parseValue( klass, null, args.get( idx ) );
    }

    protected <T> T getValue( Class<T> klass )
    {
        return getValue( klass, 0 );
    }

    protected <T> T getValue( Class<T> klass, Class<?> secondaryClass, Object value )
    {
        return QueryUtils.parseValue( klass, secondaryClass, value );
    }

    protected <T> T getValue( Class<T> klass, Object value )
    {
        return QueryUtils.parseValue( klass, value );
    }

    public boolean isValid( Class<?> klass )
    {
        return typed.isValid( klass );
    }

    public abstract Criterion getHibernateCriterion( QueryPath queryPath );

    public abstract boolean test( Object value );

    org.hibernate.criterion.MatchMode getMatchMode( com.mass3d.query.operators.MatchMode matchMode )
    {
        switch ( matchMode )
        {
        case EXACT:
            return org.hibernate.criterion.MatchMode.EXACT;
        case START:
            return org.hibernate.criterion.MatchMode.START;
        case END:
            return org.hibernate.criterion.MatchMode.END;
        case ANYWHERE:
            return org.hibernate.criterion.MatchMode.ANYWHERE;
        default:
            return null;
        }
    }

    @Override
    public String toString()
    {
        return "[" + name + ", args: " + args + "]";
    }
}
