package com.mass3d.dxf2.datavalue;

import static com.mass3d.commons.util.TextUtils.valueOf;

import org.hisp.staxwax.reader.XMLReader;
import org.hisp.staxwax.writer.XMLWriter;

public class StreamingXmlDataValue
    extends DataValue
{
    private static final String FIELD_DATAVALUE = "dataValue";
    private static final String FIELD_DATAELEMENT = "dataElement";
    private static final String FIELD_CATEGORY_OPTION_COMBO = "categoryOptionCombo";
    private static final String FIELD_ATTRIBUTE_OPTION_COMBO = "attributeOptionCombo";
    private static final String FIELD_PERIOD = "period";
    private static final String FIELD_ORGUNIT = "orgUnit";
    private static final String FIELD_VALUE = "value";
    private static final String FIELD_STOREDBY = "storedBy";
    private static final String FIELD_LAST_UPDATED = "lastUpdated";
    private static final String FIELD_COMMENT = "comment";
    private static final String FIELD_FOLLOWUP = "followUp";
    private static final String FIELD_DELETED = "deleted";

    private XMLWriter writer;

    private XMLReader reader;

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public StreamingXmlDataValue( XMLWriter writer )
    {
        this.writer = writer;

        this.writer.openElement( FIELD_DATAVALUE );
    }

    public StreamingXmlDataValue( XMLReader reader )
    {
        this.reader = reader;
    }

    //--------------------------------------------------------------------------
    // Getters
    //--------------------------------------------------------------------------

    @Override
    public String getDataElement()
    {
        return dataElement = dataElement == null ? reader.getAttributeValue( FIELD_DATAELEMENT ) : dataElement;
    }

    @Override
    public String getPeriod()
    {
        return period = period == null ? reader.getAttributeValue( FIELD_PERIOD ) : period;
    }

    @Override
    public String getOrgUnit()
    {
        return orgUnit = orgUnit == null ? reader.getAttributeValue( FIELD_ORGUNIT ) : orgUnit;
    }

    @Override
    public String getCategoryOptionCombo()
    {
        return categoryOptionCombo = categoryOptionCombo == null ? reader.getAttributeValue( FIELD_CATEGORY_OPTION_COMBO ) : categoryOptionCombo;
    }
    
    @Override
    public String getAttributeOptionCombo()
    {
        return attributeOptionCombo = attributeOptionCombo == null ? reader.getAttributeValue( FIELD_ATTRIBUTE_OPTION_COMBO ) : attributeOptionCombo;
    }
    
    @Override
    public String getValue()
    {
        return value = value == null ? reader.getAttributeValue( FIELD_VALUE ) : value;
    }

    @Override
    public String getStoredBy()
    {
        return storedBy = storedBy == null ? reader.getAttributeValue( FIELD_STOREDBY ) : storedBy;
    }

    @Override
    public String getLastUpdated()
    {
        return lastUpdated = lastUpdated == null ? reader.getAttributeValue( FIELD_LAST_UPDATED ) : lastUpdated;
    }

    @Override
    public String getComment()
    {
        return comment = comment == null ? reader.getAttributeValue( FIELD_COMMENT ) : comment;
    }

    @Override
    public Boolean getFollowup()
    {
        return followup = followup == null ? valueOf( reader.getAttributeValue( FIELD_FOLLOWUP ) ) : followup;
    }

    @Override
    public Boolean getDeleted()
    {
        return deleted = deleted == null ? valueOf( reader.getAttributeValue( FIELD_DELETED ) ) : deleted;
    }
    
    //--------------------------------------------------------------------------
    // Setters
    //--------------------------------------------------------------------------

    @Override
    public void setDataElement( String dataElement )
    {
        writer.writeAttribute( FIELD_DATAELEMENT, dataElement );
    }

    @Override
    public void setPeriod( String period )
    {
        writer.writeAttribute( FIELD_PERIOD, period );
    }

    @Override
    public void setOrgUnit( String orgUnit )
    {
        writer.writeAttribute( FIELD_ORGUNIT, orgUnit );
    }

    @Override
    public void setCategoryOptionCombo( String categoryOptionCombo )
    {
        writer.writeAttribute( FIELD_CATEGORY_OPTION_COMBO, categoryOptionCombo );
    }

    @Override
    public void setAttributeOptionCombo( String attributeOptionCombo )
    {
        writer.writeAttribute( FIELD_ATTRIBUTE_OPTION_COMBO, attributeOptionCombo );
    }
    
    @Override
    public void setValue( String value )
    {
        writer.writeAttribute( FIELD_VALUE, value );
    }

    @Override
    public void setStoredBy( String storedBy )
    {
        writer.writeAttribute( FIELD_STOREDBY, storedBy );
    }

    @Override
    public void setLastUpdated( String lastUpdated )
    {
        writer.writeAttribute( FIELD_LAST_UPDATED, lastUpdated );
    }

    @Override
    public void setComment( String comment )
    {
        writer.writeAttribute( FIELD_COMMENT, comment );
    }

    @Override
    public void setFollowup( Boolean followup )
    {
        writer.writeAttribute( FIELD_FOLLOWUP, valueOf( followup ) );
    }
    
    @Override
    public void setDeleted( Boolean deleted )
    {
        writer.writeAttribute( FIELD_DELETED, valueOf( deleted ) );
    }

    @Override
    public void close()
    {
        writer.closeElement();
    }
}
