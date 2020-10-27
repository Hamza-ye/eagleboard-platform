package com.mass3d.scheduling.parameters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Optional;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.scheduling.JobParameters;
import com.mass3d.scheduling.parameters.jackson.DataSynchronizationJobParametersDeserializer;

@JacksonXmlRootElement( localName = "jobParameters", namespace = DxfNamespaces.DXF_2_0 )
@JsonDeserialize( using = DataSynchronizationJobParametersDeserializer.class )
public class DataSynchronizationJobParameters implements JobParameters
{
    private static final long serialVersionUID = 153645562301563469L;

    static final int PAGE_SIZE_MIN = 50;
    public static final int PAGE_SIZE_MAX = 30000;

    private int pageSize = 10000;

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize( final int pageSize )
    {
        this.pageSize = pageSize;
    }

    @Override
    public Optional<ErrorReport> validate()
    {
        if ( pageSize < PAGE_SIZE_MIN || pageSize > PAGE_SIZE_MAX )
        {
            return Optional.of(
                new ErrorReport(
                    this.getClass(),
                    ErrorCode.E4008,
                    "pageSize",
                    PAGE_SIZE_MIN,
                    PAGE_SIZE_MAX,
                    pageSize )
            );
        }

        return Optional.empty();
    }
}
