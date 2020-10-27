package com.mass3d.fileresource;

import com.mass3d.common.IdentifiableObjectStore;

public interface ExternalFileResourceStore 
    extends IdentifiableObjectStore<ExternalFileResource>
{
    /**
     * Returns a single ExternalFileResource with the given (unique) accessToken.
     * 
     * @param accessToken unique string belonging to a single ExternalFileResource.
     * @return ExternalFileResource
     */
    ExternalFileResource getExternalFileResourceByAccessToken(String accessToken);
}
