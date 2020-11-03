package com.mass3d.sms.command.code;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import java.io.Serializable;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.dataelement.DataElement;
import com.mass3d.trackedentity.TrackedEntityAttribute;

@JacksonXmlRootElement( localName = "smscode", namespace = DxfNamespaces.DXF_2_0 )
public class SMSCode
    implements Serializable
{
    private int id;

    private String code;

    private DataElement dataElement;

    private TrackedEntityAttribute trackedEntityAttribute;

    private int optionId;

    private String formula;
    
    private boolean compulsory = false;

    public SMSCode( String code, DataElement dataElement, int optionId )
    {
        this.code = code;
        this.dataElement = dataElement;
        this.optionId = optionId;
    }

    public SMSCode( String code, TrackedEntityAttribute trackedEntityAttribute )
    {
        this.code = code;
        this.trackedEntityAttribute = trackedEntityAttribute;
    }

    public SMSCode()
    {
        
    }

    public boolean hasTrackedEntityAttribute()
    {
        return this.trackedEntityAttribute != null;
    }

    public boolean hasDataElement()
    {
        return this.dataElement != null;
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    @JsonProperty
    @JacksonXmlProperty
    public String getCode()
    {
        return code;
    }
    
    public void setCode( String code )
    {
        this.code = code;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( localName = "dataElement" )
    public DataElement getDataElement()
    {
        return dataElement;
    }

    public void setDataElement( DataElement dataElement )
    {
        this.dataElement = dataElement;
    }

    @JsonProperty
    @JacksonXmlProperty
    public int getOptionId()
    {
        return optionId;
    }

    public void setOptionId( int optionId )
    {
        this.optionId = optionId;
    }

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public TrackedEntityAttribute getTrackedEntityAttribute()
    {
        return trackedEntityAttribute;
    }

    public void setTrackedEntityAttribute( TrackedEntityAttribute trackedEntityAttribute )
    {
        this.trackedEntityAttribute = trackedEntityAttribute;
    }

    @JsonProperty
    @JacksonXmlProperty
    public String getFormula()
    {
        return formula;
    }

    public void setFormula( String formula )
    {
        this.formula = formula;
    }
    
    @JsonProperty
    @JacksonXmlProperty
    public boolean isCompulsory()
    {
        return compulsory;
    }

    public void setCompulsory( boolean compulsory )
    {
        this.compulsory = compulsory;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "code", code )
            .add( "dataelement", dataElement )
            .add( "trackedentityattribute", trackedEntityAttribute )
            .add( "formula", formula )
            .add( "compulsory", compulsory )
            .toString();
    }
}
