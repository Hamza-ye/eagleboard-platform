package com.mass3d.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.mass3d.schema.Schema;

public abstract class Criteria
{
    protected List<Criterion> criterions = new ArrayList<>();

    protected Set<String> aliases = new HashSet<>();

    protected final Schema schema;

    public Criteria( Schema schema )
    {
        this.schema = schema;
    }

    public List<Criterion> getCriterions()
    {
        return criterions;
    }

    public Set<String> getAliases()
    {
        return aliases;
    }

    public Criteria add( Criterion criterion )
    {
        if ( !(criterion instanceof Restriction) )
        {
            this.criterions.add( criterion ); // if conjunction/disjunction just add it and move forward
            return this;
        }

        Restriction restriction = (Restriction) criterion;

        this.criterions.add( restriction );

        return this;
    }

    public Criteria add( Criterion... criterions )
    {
        for ( Criterion criterion : criterions )
        {
            add( criterion );
        }

        return this;
    }

    public Criteria add( Collection<Criterion> criterions )
    {
        criterions.forEach( this::add );
        return this;
    }
}
