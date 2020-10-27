package com.mass3d.fileresource;

import com.google.common.collect.ImmutableSet;
import java.util.Set;

public enum FileResourceDomain
{
    DATA_VALUE( "dataValue" ),
    PUSH_ANALYSIS( "pushAnalysis" ),
    DOCUMENT( "document" ),
    MESSAGE_ATTACHMENT( "messageAttachment" ),
    USER_AVATAR( "userAvatar");

    /**
     * Container name to use when storing blobs of this FileResourceDomain
     */
    private String containerName;

    private static final Set<FileResourceDomain> DOMAIN_FOR_MULTIPLE_IMAGES =
        new ImmutableSet.Builder<FileResourceDomain>().add( DATA_VALUE, USER_AVATAR ).build();

    FileResourceDomain( String containerName )
    {
        this.containerName = containerName;
    }

    public String getContainerName()
    {
        return containerName;
    }

    public static Set<FileResourceDomain> getDomainForMultipleImages()
    {
        return DOMAIN_FOR_MULTIPLE_IMAGES;
    }
}
