package com.mass3d.dxf2.common;

import java.io.IOException;
import java.io.InputStream;
import com.mass3d.dxf2.importsummary.ImportSummaries;
import com.mass3d.dxf2.webmessage.utils.WebMessageParseUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

public class ImportSummariesResponseExtractor
    implements ResponseExtractor<ImportSummaries>
{
    @Override
    public ImportSummaries extractData( ClientHttpResponse response ) throws IOException
    {
        InputStream stream = response.getBody();

        ImportSummaries summary = null;
        
        if ( stream != null )
        {
            summary = WebMessageParseUtils.fromWebMessageResponse( stream, ImportSummaries.class );
        }
        
        return summary;
    }
}