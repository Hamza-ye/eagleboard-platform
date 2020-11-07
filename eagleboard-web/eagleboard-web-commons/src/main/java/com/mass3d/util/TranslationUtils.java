package com.mass3d.util;

import org.apache.commons.lang3.StringUtils;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.NameableObject;
import com.mass3d.dataelement.DataElement;
import com.mass3d.translation.Translation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.mass3d.system.util.ReflectionUtils.getProperty;

public class TranslationUtils
{
    public static List<String> getObjectPropertyNames( Object object )
    {
        if ( object == null )
        {
            return null;
        }

        if ( !(object instanceof IdentifiableObject) )
        {
            throw new IllegalArgumentException( "I18n object must be identifiable: " + object );
        }

        if ( object instanceof DataElement )
        {
            return Arrays.asList( DataElement.I18N_PROPERTIES );
        }

        return ( object instanceof NameableObject ) ? Arrays.asList( NameableObject.I18N_PROPERTIES ) :
            Arrays.asList( IdentifiableObject.I18N_PROPERTIES );
    }

    public static Map<String, String> getObjectPropertyValues( Object object )
    {
        if ( object == null )
        {
            return null;
        }

        List<String> properties = getObjectPropertyNames( object );

        Map<String, String> translations = new HashMap<>();

        for ( String property : properties )
        {
            translations.put( property, getProperty( object, property ) );
        }

        return translations;
    }

    public static Map<String, String> convertTranslations( Set<Translation> translations, Locale locale )
    {

        if ( !ObjectUtils.allNonNull( translations, locale ) )
        {
            return null;
        }

        Map<String, String> translationMap = new Hashtable<>();

        for ( Translation translation : translations )
        {
            if ( StringUtils.isNotEmpty( translation.getValue() ) && translation.getLocale().equalsIgnoreCase( locale.toString() ) )
            {
                translationMap.put( translation.getProperty().getName(), translation.getValue() );
            }
        }

        return translationMap;
    }
}
