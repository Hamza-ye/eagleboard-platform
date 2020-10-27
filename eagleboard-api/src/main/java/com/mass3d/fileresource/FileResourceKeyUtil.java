package com.mass3d.fileresource;

import java.util.Optional;
import java.util.UUID;

public class FileResourceKeyUtil
{

    public static String makeKey( FileResourceDomain domain, Optional<String> key )
    {
        if ( key.isPresent() )
        {
            return domain.getContainerName() + "/" + key.get();
        }
        return generateStorageKey( domain );

    }

    private static String generateStorageKey( FileResourceDomain domain )
    {
        return domain.getContainerName() + "/" + UUID.randomUUID().toString();
    }
}
