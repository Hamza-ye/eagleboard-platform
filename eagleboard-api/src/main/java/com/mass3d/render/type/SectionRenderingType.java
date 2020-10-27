package com.mass3d.render.type;

public enum SectionRenderingType
{
    LISTING,
    SEQUENTIAL,
    MATRIX;

    public static SectionRenderingType fromValue( String value )
    {
        for ( SectionRenderingType renderingType : SectionRenderingType.values() )
        {
            if ( renderingType.name().equalsIgnoreCase( value ) )
            {
                return renderingType;
            }
        }

        return null;
    }
}
