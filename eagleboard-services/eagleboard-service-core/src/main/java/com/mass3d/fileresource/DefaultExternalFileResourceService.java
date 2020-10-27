package com.mass3d.fileresource;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.common.CodeGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service( "com.mass3d.fileresource.ExternalFileResourceService" )
public class DefaultExternalFileResourceService
    implements ExternalFileResourceService
{
    private final ExternalFileResourceStore externalFileResourceStore;

    public DefaultExternalFileResourceService( ExternalFileResourceStore externalFileResourceStore )
    {
        checkNotNull( externalFileResourceStore );

        this.externalFileResourceStore = externalFileResourceStore;
    }

    @Override
    @Transactional(readOnly = true)
    public ExternalFileResource getExternalFileResourceByAccessToken( String accessToken )
    {
        return externalFileResourceStore.getExternalFileResourceByAccessToken( accessToken );
    }

    @Override
    @Transactional
    public String saveExternalFileResource( ExternalFileResource externalFileResource )
    {
        Assert.notNull( externalFileResource, "External file resource cannot be null" );
        Assert.notNull( externalFileResource.getFileResource(), "External file resource entity cannot be null" );

        externalFileResource.setAccessToken( CodeGenerator.getRandomUrlToken() );

        externalFileResourceStore.save( externalFileResource );

        return externalFileResource.getAccessToken();
    }
}
