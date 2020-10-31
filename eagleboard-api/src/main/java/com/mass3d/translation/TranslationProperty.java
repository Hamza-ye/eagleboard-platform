package com.mass3d.translation;

public enum TranslationProperty
{
    NAME ( "name" ),
    SHORT_NAME ( "shortName" ),
    DESCRIPTION ("description" ),
    FORM_NAME ( "formName" ),
    NUMERATOR_DESCRIPTION ( "numeratorDescription" ),
    DENOMINATOR_DESCRIPTION ( "denominatorDescription" ),
    RELATIONSHIP_FROM_TO_NAME ("fromToName" ),
    RELATIONSHIP_TO_FROM_NAME ("toFromName" );

    private String name;

    TranslationProperty( String name )
    {
        this.name = name;
    }

    public static TranslationProperty fromValue( String value )
    {
        for ( TranslationProperty type : TranslationProperty.values() )
        {
            if ( type.getName().equalsIgnoreCase( value ) )
            {
                return type;
            }
        }

        return null;
    }

    public String getName()
    {
        return name;
    }
}
