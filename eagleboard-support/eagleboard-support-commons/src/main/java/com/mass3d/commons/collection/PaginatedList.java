package com.mass3d.commons.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * List implementation that provides a paged view.
 *
 */
public class PaginatedList<T>
    extends ArrayList<T>
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 6296545460322554660L;

    public static final int DEFAULT_PAGE_SIZE = 10;

    private int pageSize;

    private int fromIndex = 0;

    public PaginatedList( Collection<T> collection )
    {
        super( collection );
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    /**
     * Sets page size.
     *
     * @param pageSize the page size.
     * @return this PaginatedList.
     */
    public PaginatedList<T> setPageSize( int pageSize )
    {
        this.pageSize = pageSize;

        return this;
    }

    /**
     * Sets the number of pages. The page size will be calculated and set in
     * order to provide the appropriate total number of pages. The resulting
     * number of pages can be lower than the given argument but not higher.
     *
     * @param pages the number of pages.
     * @return this PaginatedList
     */
    public PaginatedList<T> setNumberOfPages( int pages )
    {
        this.pageSize = (int) Math.ceil( (double) size() / pages );

        return this;
    }

    /**
     * Returns the next page in the list. The page size is defined by the argument
     * given in the constructor. If there is no more pages, null is returned. The
     * returned page is not guaranteed to have the same size as the page size.
     *
     * @return the next page.
     */
    public List<T> nextPage()
    {
        int size = size();

        if ( fromIndex >= size )
        {
            return null;
        }

        int toIndex = Math.min( ( fromIndex + pageSize ), size );

        List<T> page = subList( fromIndex, toIndex );

        fromIndex = toIndex;

        return page;
    }

    /**
     * Sets the current page position to the first page.
     */
    public void reset()
    {
        fromIndex = 0;
    }

    /**
     * Returns the number of pages in the list.
     *
     * @return the number of pages.
     */
    public int pageCount()
    {
        int count = size();
        int pages = count / pageSize;
        int mod = count % pageSize;

        return mod == 0 ? pages : ( pages + 1 );
    }

    /**
     * Returns a list of all pages.
     *
     * @return a List of all pages.
     */
    public List<List<T>> getPages()
    {
        List<List<T>> pages = new ArrayList<>();

        List<T> page = null;

        while ( ( page = nextPage() ) != null )
        {
            pages.add( page );
        }

        return pages;
    }
}
