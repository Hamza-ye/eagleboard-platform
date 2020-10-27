package com.mass3d.dxf2.datavalueset;

import com.mass3d.dxf2.datavalue.DataValue;
import com.mass3d.dxf2.datavalue.StreamingXmlDataValue;
import org.hisp.staxwax.reader.XMLReader;
import org.hisp.staxwax.writer.XMLWriter;

public class StreamingXmlDataValueSet
    extends DataValueSet
{
    private static final String XMLNS = "xmlns";
    private static final String NS = "http://dhis2.org/schema/dxf/2.0";
    private static final String TRUE = "true";

    private XMLWriter writer;

    private XMLReader reader;

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public StreamingXmlDataValueSet( XMLWriter writer )
    {
        this.writer = writer;

        this.writer.openDocument();
        this.writer.openElement( FIELD_DATAVALUESET );
        this.writer.writeAttribute( XMLNS, NS );
    }

    public StreamingXmlDataValueSet( XMLReader reader )
    {
        this.reader = reader;
        this.reader.moveToStartElement( FIELD_DATAVALUESET );
    }

    //--------------------------------------------------------------------------
    // Getters
    //--------------------------------------------------------------------------

    @Override
    public String getIdScheme()
    {
        return idScheme = idScheme == null ? reader.getAttributeValue( FIELD_IDSCHEME ) : idScheme;
    }
    
    @Override
    public String getDataElementIdScheme()
    {
        return dataElementIdScheme = dataElementIdScheme == null ? reader.getAttributeValue( FIELD_DATAELEMENTIDSCHEME ) : dataElementIdScheme;
    }

    @Override
    public String getOrgUnitIdScheme()
    {
        return orgUnitIdScheme = orgUnitIdScheme == null ? reader.getAttributeValue( FIELD_ORGUNITIDSCHEME ) : orgUnitIdScheme;
    }

    @Override
    public String getCategoryOptionComboIdScheme()
    {
        return categoryOptionComboIdScheme = categoryOptionComboIdScheme == null ? reader.getAttributeValue( FIELD_CATEGORYOPTCOMBOIDSCHEME ) : categoryOptionComboIdScheme;
    }

    @Override
    public String getDataSetIdScheme()
    {
        return dataSetIdScheme = dataSetIdScheme == null ? reader.getAttributeValue( FIELD_DATASETIDSCHEME ) : dataSetIdScheme;
    }
    
    @Override
    public Boolean getDryRun()
    {
        return dryRun = dryRun == null ? ( TRUE.equals( reader.getAttributeValue( FIELD_DRYRUN ) ) ? Boolean.TRUE : null ) : dryRun;
    }

    @Override
    public String getStrategy()
    {
        return strategy = strategy == null ? reader.getAttributeValue( FIELD_IMPORTSTRATEGY ) : strategy;
    }

    @Override
    public String getDataSet()
    {
        return dataSet = dataSet == null ? reader.getAttributeValue( FIELD_DATASET ) : dataSet;
    }

    @Override
    public String getCompleteDate()
    {
        return completeDate = completeDate == null ? reader.getAttributeValue( FIELD_COMPLETEDATE ) : completeDate;
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
    public String getAttributeOptionCombo()
    {
        return attributeOptionCombo = attributeOptionCombo == null ? reader.getAttributeValue( FIELD_ATTRIBUTE_OPTION_COMBO ) : attributeOptionCombo;
    }
    
    @Override
    public boolean hasNextDataValue()
    {
        return reader.moveToStartElement( FIELD_DATAVALUE, FIELD_DATAVALUESET );
    }

    @Override
    public DataValue getNextDataValue()
    {
        return new StreamingXmlDataValue( reader );
    }

    //--------------------------------------------------------------------------
    // Setters
    //--------------------------------------------------------------------------

    @Override
    public void setIdScheme( String idScheme )
    {
        writer.writeAttribute( FIELD_IDSCHEME, idScheme );
    }
    
    @Override
    public void setDataElementIdScheme( String dataElementIdScheme )
    {
        writer.writeAttribute( FIELD_DATAELEMENTIDSCHEME, dataElementIdScheme );
    }

    @Override
    public void setOrgUnitIdScheme( String orgUnitIdScheme )
    {
        writer.writeAttribute( FIELD_ORGUNITIDSCHEME, orgUnitIdScheme );
    }

    @Override
    public void setCategoryOptionComboIdScheme( String categoryOptionComboIdScheme )
    {
        writer.writeAttribute( FIELD_CATEGORYOPTCOMBOIDSCHEME, categoryOptionComboIdScheme );
    }
    
    @Override
    public void setDataSetIdScheme( String dataSetIdScheme )
    {
        writer.writeAttribute( FIELD_DATASETIDSCHEME, dataSetIdScheme );
    }

    @Override
    public void setDataSet( String dataSet )
    {
        writer.writeAttribute( FIELD_DATASET, dataSet );
    }

    @Override
    public void setCompleteDate( String completeDate )
    {
        writer.writeAttribute( FIELD_COMPLETEDATE, completeDate );
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
    public DataValue getDataValueInstance()
    {
        return new StreamingXmlDataValue( writer );
    }

    @Override
    public void close()
    {
        if ( writer != null )
        {
            writer.closeElement();
            writer.closeDocument();
        }
    }
}
