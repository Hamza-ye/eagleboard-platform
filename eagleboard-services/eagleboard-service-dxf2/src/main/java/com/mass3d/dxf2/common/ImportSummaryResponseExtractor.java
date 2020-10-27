package com.mass3d.dxf2.common;

import com.mass3d.commons.config.JacksonObjectMapperConfig;
import com.mass3d.dxf2.importsummary.ImportSummary;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;
import java.io.IOException;
/**
 * Converts a response into an ImportSummary instance.
 *
 * @throws HttpServerErrorException if the response status code is different
 * from 200 OK or 201 Created.
 * @throws IOException if converting the response into an ImportSummary failed.
 */
public class ImportSummaryResponseExtractor
    implements ResponseExtractor<ImportSummary>
{
    @Override
    public ImportSummary extractData( ClientHttpResponse response ) throws IOException
    {
        return JacksonObjectMapperConfig.staticJsonMapper().readValue( response.getBody(), ImportSummary.class );
    }
}
