package com.mass3d.query;

/**
 * Simple POJO containing the pagination directive from the HTTP Request
 * 
 */
public class Pagination
{
    private int firstResult;

    private int size;

    private boolean hasPagination = false;

    public Pagination( int firstResult, int size )
    {
        assert ( size > 0 );
        this.firstResult = firstResult;
        this.size = size;
        this.hasPagination = true;
    }

    /**
     * This constructor can be used to signal that there is no pagination data
     */
    public Pagination()
    {
        // empty constructor
    }

    public int getFirstResult()
    {
        return firstResult;
    }

    public int getSize()
    {
        return size;
    }

    public boolean hasPagination()
    {
        return hasPagination;
    }
}
