package com.mass3d.deduplication;

import com.google.common.base.MoreObjects;
import java.util.List;
import org.apache.commons.lang.BooleanUtils;
import com.mass3d.common.Pager;
import com.mass3d.common.PagerUtils;

public class PotentialDuplicateQuery
{

    public static final PotentialDuplicateQuery EMPTY = new PotentialDuplicateQuery();

    private Boolean skipPaging;

    private Boolean paging;

    private int page = 1;

    private int pageSize = Pager.DEFAULT_PAGE_SIZE;

    private int total;

    private List<String> teis;

    public PotentialDuplicateQuery()
    {
    }

    public boolean isSkipPaging()
    {
        return PagerUtils.isSkipPaging( skipPaging, paging );
    }

    public void setSkipPaging( Boolean skipPaging )
    {
        this.skipPaging = skipPaging;
    }

    public boolean isPaging()
    {
        return BooleanUtils.toBoolean( paging );
    }

    public void setPaging( Boolean paging )
    {
        this.paging = paging;
    }

    public int getPage()
    {
        return page;
    }

    public void setPage( int page )
    {
        this.page = page;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize( int pageSize )
    {
        this.pageSize = pageSize;
    }

    public int getTotal()
    {
        return total;
    }

    public void setTotal( int total )
    {
        this.total = total;
    }

    public Pager getPager()
    {
        return PagerUtils.isSkipPaging( skipPaging, paging ) ? null : new Pager( page, total, pageSize );
    }

    public List<String> getTeis()
    {
        return teis;
    }

    public void setTeis( List<String> teis )
    {
        this.teis = teis;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "page", page )
            .add( "pageSize", pageSize )
            .add( "total", total )
            .toString();
    }
}
