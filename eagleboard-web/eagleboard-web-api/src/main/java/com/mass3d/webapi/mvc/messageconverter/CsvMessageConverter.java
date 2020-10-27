package com.mass3d.webapi.mvc.messageconverter;

import com.google.common.collect.ImmutableList;
import com.mass3d.common.Compression;
import com.mass3d.node.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.annotation.Nonnull;

public class CsvMessageConverter extends AbstractRootNodeMessageConverter
{
    public static final ImmutableList<MediaType> SUPPORTED_MEDIA_TYPES = ImmutableList.<MediaType>builder()
        .add( new MediaType( "application", "csv" ) )
        .add( new MediaType( "text", "csv" ) )
        .build();

    public static final ImmutableList<MediaType> GZIP_SUPPORTED_MEDIA_TYPES = ImmutableList.<MediaType>builder()
        .add( new MediaType( "application", "csv+gzip" ) )
        .build();

    public static final ImmutableList<MediaType> ZIP_SUPPORTED_MEDIA_TYPES = ImmutableList.<MediaType>builder()
        .add( new MediaType( "application", "csv+zip" ) )
        .build();

    public CsvMessageConverter( @Autowired @Nonnull NodeService nodeService, Compression compression )
    {
        super( nodeService, "application/csv", "csv", compression );
        switch ( getCompression() )
        {
            case NONE:
                setSupportedMediaTypes( SUPPORTED_MEDIA_TYPES );
                break;
            case GZIP:
                setSupportedMediaTypes( GZIP_SUPPORTED_MEDIA_TYPES );
                break;
            case ZIP:
                setSupportedMediaTypes( ZIP_SUPPORTED_MEDIA_TYPES );
        }
    }
}
