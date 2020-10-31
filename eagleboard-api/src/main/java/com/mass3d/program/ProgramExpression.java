package com.mass3d.program;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement( localName = "programExpression", namespace = DxfNamespaces.DXF_2_0 )
public class ProgramExpression
    implements Serializable
{
    public static final String SEPARATOR_ID = "\\.";
    public static final String SEPARATOR_OBJECT = ":";
    public static final String DUE_DATE = "DUE_DATE";
    public static final String REPORT_DATE = "REPORT_DATE";
    public static final String RANGE_IN_DUE_DATE = "RANGE_IN_DUE_DATE";
    public static final String NOT_NULL_VALUE_IN_EXPRESSION = "NOT-NULL-VALUE";
    public static final String OBJECT_PROGRAM_STAGE_DATAELEMENT = "DE";
    public static final String OBJECT_PROGRAM_STAGE = "PS";

    private long id;

    private String expression;

    private String description;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ProgramExpression()
    {
    }

    public ProgramExpression( String expression, String description )
    {
        this.expression = expression;
        this.description = description;
    }

    // -------------------------------------------------------------------------
    // Equals and hashCode
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((expression == null) ? 0 : expression.hashCode());
        return result;
    }

    @Override
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }

        if ( object == null )
        {
            return false;
        }

        if ( getClass() != object.getClass() )
        {
            return false;
        }

        final ProgramExpression other = (ProgramExpression) object;

        if ( description == null )
        {
            if ( other.description != null )
            {
                return false;
            }
        }
        else if ( !description.equals( other.description ) )
        {
            return false;
        }

        if ( expression == null )
        {
            if ( other.expression != null )
            {
                return false;
            }
        }
        else if ( !expression.equals( other.expression ) )
        {
            return false;
        }

        return true;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public long getId()
    {
        return id;
    }

    public void setId( long id )
    {
        this.id = id;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getExpression()
    {
        return expression;
    }

    public void setExpression( String expression )
    {
        this.expression = expression;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }
}
