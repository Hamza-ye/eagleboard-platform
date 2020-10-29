package com.mass3d.commons.action;

import org.apache.struts2.ServletActionContext;
import com.mass3d.common.IdentifiableObjectUtils;
import com.mass3d.dataset.DataSet;
import com.mass3d.dataset.DataSetService;
import com.mass3d.indicator.Indicator;
import com.mass3d.indicator.IndicatorGroup;
import com.mass3d.indicator.IndicatorService;
import com.mass3d.paging.ActionPagingSupport;
import com.mass3d.util.ContextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetIndicatorsAction
    extends ActionPagingSupport<Indicator>
{
    private final static int ALL = 0;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }

    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    // -------------------------------------------------------------------------
    // Input & output
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    private Integer dataSetId;

    public void setDataSetId( Integer dataSetId )
    {
        this.dataSetId = dataSetId;
    }

    private String key;

    public void setKey( String key )
    {
        this.key = key;
    }

    private List<Indicator> indicators;

    public List<Indicator> getIndicators()
    {
        return indicators;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    @Override
    public String execute()
        throws Exception
    {
        if ( id != null && id != ALL )
        {
            IndicatorGroup indicatorGroup = indicatorService.getIndicatorGroup( id );

            if ( indicatorGroup != null )
            {
                indicators = new ArrayList<>( indicatorGroup.getMembers() );
            }
        }
        else if ( dataSetId != null )
        {
            DataSet dataset = dataSetService.getDataSet( dataSetId );

            if ( dataset != null )
            {
                indicators = new ArrayList<>( dataset.getIndicators() );
            }
        }
        else
        {
            indicators = new ArrayList<>( indicatorService.getAllIndicators() );

            ContextUtils.clearIfNotModified( ServletActionContext.getRequest(), ServletActionContext.getResponse(), indicators );
        }

        if ( key != null )
        {
            indicators = IdentifiableObjectUtils.filterNameByKey( indicators, key, true );
        }

        if ( indicators == null )
        {
            indicators = new ArrayList<>();
        }

        Collections.sort( indicators );

        if ( usePaging )
        {
            this.paging = createPaging( indicators.size() );

            indicators = indicators.subList( paging.getStartPos(), paging.getEndPos() );
        }

        return SUCCESS;
    }
}
