package com.mass3d.fileresource;

import java.util.Optional;

public enum ImageFileDimension
{
    SMALL( "small" ),
    MEDIUM( "medium" ),
    LARGE( "large" ),
    ORIGINAL( "" );

    private String dimension;

    ImageFileDimension( String dimension )
    {
        this.dimension = dimension;
    }

    public String getDimension()
    {
        return this.dimension;
    }

    public static Optional<ImageFileDimension> from( String dimension )
    {
        for ( ImageFileDimension d : ImageFileDimension.values() )
        {
            if ( d.dimension.equalsIgnoreCase( dimension ) )
            {
                return Optional.of( d );
            }
        }

        return Optional.empty();
    }
}
