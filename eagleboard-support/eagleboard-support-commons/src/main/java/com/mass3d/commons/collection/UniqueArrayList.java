package com.mass3d.commons.collection;

import java.util.ArrayList;
import java.util.Collection;

/**
 * List implementation that will only add items which are not already present in
 * the list.
 *
 */
public class UniqueArrayList<E>
    extends ArrayList<E>
{
    public UniqueArrayList()
    {
        super();
    }

    public UniqueArrayList( Collection<? extends E> c )
    {
        super();
        addAll( c );
    }

    @Override
    public boolean add( E e )
    {
        return super.contains( e ) ? false : super.add( e );
    }

    @Override
    public void add( int index, E e )
    {
        if ( !super.contains( e ) )
        {
            super.add( index, e );
        }
    }

    @Override
    public boolean addAll( Collection<? extends E> c )
    {
        boolean modified = false;

        if ( c != null )
        {
            for ( E e : c )
            {
                if ( !super.contains( e ) )
                {
                    super.add( e );
                    modified = true;
                }
            }
        }

        return modified;
    }

    @Override
    public boolean addAll( int index, Collection<? extends E> c )
    {
        boolean modified = false;

        if ( c != null )
        {
            for ( E e : c )
            {
                if ( !super.contains( e ) )
                {
                    super.add( index++, e );
                    modified = true;
                }
            }
        }

        return modified;
    }
}
