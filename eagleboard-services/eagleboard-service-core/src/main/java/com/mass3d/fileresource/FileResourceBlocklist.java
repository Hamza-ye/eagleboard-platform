package com.mass3d.fileresource;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.io.FilenameUtils;

public class FileResourceBlocklist
{
    private static final ImmutableSet<String> CONTENT_TYPES = ImmutableSet.of(
        // Web
        "text/html",
        "text/css",
        "text/javascript",
        "font/otf",
        "application/x-shockwave-flash",
        // Executable
        "application/vnd.debian.binary-package",
        "application/x-rpm",
        "application/java-archive",
        "application/x-ms-dos-executable",
        "application/vnd.microsoft.portable-executable",
        "application/vnd.apple.installer+xml",
        "application/vnd.mozilla.xul+xml",
        "application/x-httpd-php",
        "application/x-sh",
        "application/x-csh"
    );

    private static final ImmutableSet<String> FILE_EXTENSIONS = ImmutableSet.of(
        // Web
        "html",
        "htm",
        "css",
        "js",
        "mjs",
        "otf",
        "swf",
        // Executable
        "deb",
        "rpm",
        "jar",
        "jsp",
        "exe",
        "msi",
        "mpkg",
        "xul",
        "php",
        "bin",
        "sh",
        "csh",
        "bat"
    );

    /**
     * Indicates whether the given file resource has a valid file extension and content type
     * according to the blacklist.
     *
     * @param fileResource the {@link FileResource}.
     * @return true if valid, false if invalid.
     */
    public static boolean isValid( FileResource fileResource )
    {
        if ( fileResource == null || fileResource.getContentType() == null || fileResource.getName() == null )
        {
            return false;
        }

        if ( CONTENT_TYPES.contains( fileResource.getContentType().toLowerCase() ) )
        {
            return false;
        }

        if ( FILE_EXTENSIONS.contains( FilenameUtils.getExtension( fileResource.getName().toLowerCase() ) ) )
        {
            return false;
        }

        return true;
    }
}
