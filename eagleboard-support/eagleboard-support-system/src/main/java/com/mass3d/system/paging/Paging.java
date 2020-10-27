package com.mass3d.system.paging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.mass3d.common.IdentifiableObject;

public class Paging
{
    public static final int DEFAULT_PAGE_SIZE = 50;
    private static final int PAGE_OFFSET = 2; // Each side of current page
    private static final int PAGE_TOTAL_OFFSET = PAGE_OFFSET * 2; // Both sides of current page

    private int currentPage;

    private int pageSize;

    private int total;

    private String link;

    public Paging()
    {
    }

    public Paging( String link, int pageSize )
    {
        currentPage = 1;
        this.pageSize = pageSize;
        total = 0;
        this.link = link;
    }

    public String getBaseLink()
    {
        return link.indexOf( "?" ) < 0 ? ( link + "?" ) : ( link + "&" );
    }

    public int getNumberOfPages()
    {
        return  total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
    }

    /**
     * Returns first page in paging range.
     */
    public int getStartPage()
    {
        int startPage = 1;

        if ( currentPage > PAGE_OFFSET ) // Far enough from start, set start page
        {
            startPage = currentPage - PAGE_OFFSET;
        }

        if ( ( getNumberOfPages() - startPage ) < PAGE_TOTAL_OFFSET ) // Too close to end, decrease start page to maintain page range length
        {
            startPage = getNumberOfPages() - PAGE_TOTAL_OFFSET;
        }

        if ( startPage <= 0 ) // Cannnot be 0 or less, set start page to 1
        {
            startPage = 1;
        }

        return startPage;
    }

    /**
     * Returns first row number in paged table for current page.
     */
    public int getStartPos()
    {
        int startPos = currentPage <= 0 ? 0 : ( currentPage - 1 ) * pageSize;
        startPos = ( startPos >  total ) ? total : startPos;
        return startPos;
    }

    /**
     * Returns last row number in paged table for current page.
     */
    public int getEndPos()
    {
    	int endPos = getStartPos() + pageSize;
        endPos = ( endPos > total ) ? total : endPos;
        return endPos;
    }

    public int getCurrentPage()
    {
        if ( currentPage > getNumberOfPages() )
        {
            currentPage = getNumberOfPages();
        }

        return currentPage;
    }

    public void setCurrentPage( int currentPage )
    {
        this.currentPage = currentPage > 0 ? currentPage : 1;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize( int pageSize )
    {
        this.pageSize = pageSize > 0 ? pageSize : Paging.DEFAULT_PAGE_SIZE;
    }

    public int getTotal()
    {
        return total;
    }

    public void setTotal( int total )
    {
        this.total = total;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink( String link )
    {
        this.link = link;
    }

    // -------------------------------------------------------------------------
    // Paging static utility methods
    // -------------------------------------------------------------------------

    public static <T extends IdentifiableObject> int getCountByName( Collection<T> objects, String name )
    {
        int count = 0;

        if ( name != null )
        {
            for ( IdentifiableObject object : objects )
            {
                if ( object != null && object.getDisplayName() != null && object.getDisplayName().toLowerCase().contains( name.toLowerCase() ) )
                {
                    count++;
                }
            }
        }

        return count;
    }

    public static <T extends IdentifiableObject> List<T> getObjectsBetween( Collection<T> objects, int first, int max )
    {
        final List<T> list = new ArrayList<>( objects );

        Collections.sort( list );

        int last = first + max;

        last = last < list.size() ? last : list.size();

        return list.subList( first, last );
    }

    public static <T extends IdentifiableObject> List<T> getObjectsBetweenByName( Collection<T> objects, String name, int first, int max )
    {
        final List<T> list = new ArrayList<>();

        if ( name != null )
        {
            for ( T object : objects )
            {
                if ( object != null && object.getDisplayName() != null && object.getDisplayName().toLowerCase().contains( name.toLowerCase() ) )
                {
                    list.add( object );
                }
            }
        }

        Collections.sort( list );

        int last = first + max;

        last = last < list.size() ? last : list.size();

        return list.subList( first, last );
    }

    public static <T extends IdentifiableObject> List<T> getObjectsByName( Collection<T> objects, String name )
    {
        final List<T> list = new ArrayList<>();

        if ( name != null )
        {
            for ( T object : objects )
            {
                if ( object != null && object.getDisplayName() != null && object.getDisplayName().toLowerCase().contains( name.toLowerCase() ) )
                {
                    list.add( object );
                }
            }
        }

        return list;
    }
}
