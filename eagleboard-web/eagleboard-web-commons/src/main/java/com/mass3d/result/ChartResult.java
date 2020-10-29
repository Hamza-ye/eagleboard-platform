package com.mass3d.result;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


import org.apache.struts2.ServletActionContext;
import com.mass3d.util.ContextUtils;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * A custom Result type for chart data. Built on top of <a
 * href="http://www.jfree.org/jfreechart/" target="_blank">JFreeChart</a>. When
 * executed this Result will write the given chart as a PNG to the servlet
 * output stream.
 *
 */
@Slf4j
public class ChartResult
    implements Result
{
    private static final String DEFAULT_FILENAME = "chart.png";
    private static final int DEFAULT_WIDTH = 700;
    private static final int DEFAULT_HEIGHT = 500;

    private JFreeChart chart = null;

    private Integer height;

    private Integer width;

    private String filename;

    /**
     * Sets the JFreeChart to use.
     *
     * @param chart a JFreeChart object.
     */
    public void setChart( JFreeChart chart )
    {
        this.chart = chart;
    }

    /**
     * Sets the chart height.
     *
     * @param height the height of the chart in pixels.
     */
    public void setHeight( Integer height )
    {
        this.height = height;
    }

    /**
     * Sets the chart width.
     *
     * @param width the width of the chart in pixels.
     */
    public void setWidth( Integer width )
    {
        this.width = width;
    }

    /**
     * Sets the filename.
     *
     * @param filename the filename.
     */
    public void setFilename( String filename )
    {
        this.filename = filename;
    }

    /**
     * Executes the result. Writes the given chart as a PNG to the servlet
     * output stream.
     *
     * @param invocation an encapsulation of the action execution state.
     * @throws Exception if an error occurs when creating or writing the chart
     *                   to the servlet output stream.
     */
    @Override
    public void execute( ActionInvocation invocation )
        throws Exception
    {
        JFreeChart stackChart = (JFreeChart) invocation.getStack().findValue( "chart" );

        chart = stackChart != null ? stackChart : chart;

        Integer stackHeight = (Integer) invocation.getStack().findValue( "height" );

        height = stackHeight != null && stackHeight > 0 ? stackHeight : height != null ? height : DEFAULT_HEIGHT;

        Integer stackWidth = (Integer) invocation.getStack().findValue( "width" );

        width = stackWidth != null && stackWidth > 0 ? stackWidth : width != null ? width : DEFAULT_WIDTH;

        String stackFilename = (String) invocation.getStack().findValue( "filename" );

        filename = StringUtils.defaultIfEmpty( stackFilename, DEFAULT_FILENAME );

        if ( chart == null )
        {
            log.warn( "No chart found" );
            return;
        }

        HttpServletResponse response = ServletActionContext.getResponse();
        ContextUtils.configureResponse( response, ContextUtils.CONTENT_TYPE_PNG, true, filename, false );

        OutputStream os = response.getOutputStream();
        ChartUtils.writeChartAsPNG( os, chart, width, height );
        os.flush();
    }
}
