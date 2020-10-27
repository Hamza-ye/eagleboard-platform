package com.mass3d.webapi.mvc.messageconverter;

import com.google.common.collect.ImmutableList;
import com.mass3d.common.Compression;
import com.mass3d.node.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class ExcelMessageConverter extends AbstractRootNodeMessageConverter
{
    public static final ImmutableList<MediaType> SUPPORTED_MEDIA_TYPES = ImmutableList.<MediaType>builder()
        .add( new MediaType( "application", "vnd.ms-excel" ) )
        .build();

    public ExcelMessageConverter( @Nonnull @Autowired NodeService nodeService )
    {
        super( nodeService, "application/vnd.ms-excel", "xlsx", Compression.NONE );
        setSupportedMediaTypes( SUPPORTED_MEDIA_TYPES );
    }
}
