package com.mass3d.dataentryform;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Objects;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DisplayDensity;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;

@JacksonXmlRootElement( localName = "dataEntryForm", namespace = DxfNamespaces.DXF_2_0 )
public class DataEntryForm
    extends BaseIdentifiableObject implements MetadataObject
{
    public static final int CURRENT_FORMAT = 2;

    /**
     * Name of DataEntryForm. Required and unique.
     */
    private String name;

    /**
     * The display style to use to render the form.
     */
    private DisplayDensity style;

    /**
     * HTML Code of DataEntryForm
     */
    private String htmlCode;

    /**
     * The format of the DataEntryForm.
     */
    private int format;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public DataEntryForm()
    {

    }

    public DataEntryForm( String name )
    {
        this();
        this.name = name;
    }

    public DataEntryForm( String name, String htmlCode )
    {
        this( name );
        this.htmlCode = htmlCode;
    }

    public DataEntryForm( String name, DisplayDensity style, String htmlCode )
    {
        this( name, htmlCode );
        this.style = style;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    /**
     * Indicates whether this data entry form has custom form HTML code.
     */
    public boolean hasForm()
    {
        return htmlCode != null && !htmlCode.trim().isEmpty();
    }

    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        return 31 * super.hashCode() + Objects.hash( name, style, htmlCode, format );
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null || getClass() != obj.getClass() )
        {
            return false;
        }
        if ( !super.equals( obj ) )
        {
            return false;
        }
        final DataEntryForm other = (DataEntryForm) obj;
        return Objects.equals( this.name, other.name )
            && Objects.equals( this.style, other.style )
            && Objects.equals( this.htmlCode, other.htmlCode )
            && Objects.equals( this.format, other.format );
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public DisplayDensity getStyle()
    {
        return style;
    }

    public void setStyle( DisplayDensity style )
    {
        this.style = style;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getHtmlCode()
    {
        return htmlCode;
    }

    public void setHtmlCode( String htmlCode )
    {
        this.htmlCode = htmlCode;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getFormat()
    {
        return format;
    }

    public void setFormat( int format )
    {
        this.format = format;
    }
}
