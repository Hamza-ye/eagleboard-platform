package com.mass3d.feedback;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.IdentifiableObject;

@JacksonXmlRootElement( localName = "objectReport", namespace = DxfNamespaces.DXF_2_0 )
public class ObjectReport
{
    private final Class<?> klass;

    private Integer index;

    /**
     * UID of object (if object is id object).
     */
    private String uid;

    /**
     * Name to be used if ImportReportMode is DEBUG
     */
    private String displayName;

    private Map<ErrorCode, List<ErrorReport>> errorReportsByCode = new HashMap<>();

    public ObjectReport( @Nonnull IdentifiableObject object, @Nonnull ObjectIndexProvider objectIndexProvider )
    {
        this( object, objectIndexProvider, null );
    }

    public ObjectReport( @Nonnull IdentifiableObject object, @Nonnull ObjectIndexProvider objectIndexProvider, @Nullable String uid )
    {
        this( object.getClass(), objectIndexProvider.mergeObjectIndex( object ), uid == null ? object.getUid() : uid );
    }

    @JsonCreator
    public ObjectReport( @JsonProperty( "klass" ) Class<?> klass, @JsonProperty( "index" ) Integer index )
    {
        this.klass = klass;
        this.index = index;
    }

    public ObjectReport( Class<?> klass, Integer index, String uid )
    {
        this.klass = klass;
        this.index = index;
        this.uid = uid;
    }

    public ObjectReport( Class<?> klass, Integer index, String uid, String displayName )
    {
        this.klass = klass;
        this.index = index;
        this.uid = uid;
        this.displayName = displayName;
    }

    public ObjectReport( ObjectReport objectReport )
    {
        this.klass = objectReport.getKlass();
        this.index = objectReport.getIndex();
        this.uid = objectReport.getUid();
        this.displayName = objectReport.getDisplayName();
    }

    //-----------------------------------------------------------------------------------
    // Utility Methods
    //-----------------------------------------------------------------------------------

    public void merge( ObjectReport objectReport )
    {
        addErrorReports( objectReport.getErrorReports() );
    }

    public void addErrorReports( List<? extends ErrorReport> errorReports )
    {
        errorReports.forEach( this::addErrorReport );
    }

    public void addErrorReport( ErrorReport errorReport )
    {
        if ( !errorReportsByCode.containsKey( errorReport.getErrorCode() ) )
        {
            errorReportsByCode.put( errorReport.getErrorCode(), new ArrayList<>() );
        }

        errorReportsByCode.get( errorReport.getErrorCode() ).add( errorReport );
    }

    //-----------------------------------------------------------------------------------
    // Getters and Setters
    //-----------------------------------------------------------------------------------

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public Class<?> getKlass()
    {
        return klass;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public Integer getIndex()
    {
        return index;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public String getUid()
    {
        return uid;
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName( String displayName )
    {
        this.displayName = displayName;
    }

    @JsonProperty
    @JacksonXmlElementWrapper( localName = "errorReports", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "errorReport", namespace = DxfNamespaces.DXF_2_0 )
    public List<ErrorReport> getErrorReports()
    {
        List<ErrorReport> errorReports = new ArrayList<>();
        errorReportsByCode.values().forEach( errorReports::addAll );

        return errorReports;
    }
    
    @JsonProperty
    @JacksonXmlElementWrapper( localName = "errorReports", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "errorReport", namespace = DxfNamespaces.DXF_2_0 )
    public void setErrorReports( List<ErrorReport> errorReports )
    {
        if ( errorReports != null )
        {
            errorReports.forEach( er -> {
                List<ErrorReport> errorReportForCode = errorReportsByCode.get( er.getErrorCode() );
                if ( errorReportForCode == null )
                {
                    errorReportForCode = new ArrayList<>();
                }
                errorReportForCode.add( er );
                errorReportsByCode.put( er.getErrorCode(), errorReportForCode );
            } );
        }
    }

    public List<ErrorCode> getErrorCodes()
    {
        return new ArrayList<>( errorReportsByCode.keySet() );
    }

    public Map<ErrorCode, List<ErrorReport>> getErrorReportsByCode()
    {
        return errorReportsByCode;
    }

    public boolean isEmpty()
    {
        return errorReportsByCode.isEmpty();
    }

    public int size()
    {
        return errorReportsByCode.size();
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "klass", klass )
            .add( "index", index )
            .add( "uid", uid )
            .add( "errorReports", getErrorReports() )
            .toString();
    }
}
