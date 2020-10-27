package com.mass3d.webapi.mvc.messageconverter;

import com.google.common.collect.ImmutableList;
import com.mass3d.common.Compression;
import com.mass3d.node.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class PdfMessageConverter extends AbstractRootNodeMessageConverter
{
    public static final ImmutableList<MediaType> SUPPORTED_MEDIA_TYPES = ImmutableList.<MediaType>builder()
        .add( new MediaType( "application", "pdf" ) )
        .build();

    public PdfMessageConverter( @Nonnull @Autowired NodeService nodeService )
    {
        super( nodeService, "application/pdf", "pdf", Compression.NONE );
        setSupportedMediaTypes( SUPPORTED_MEDIA_TYPES );
    }
}
