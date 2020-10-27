package com.mass3d.setting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import com.mass3d.commons.config.JacksonObjectMapperConfig;

@Slf4j
public class SystemSetting
    implements Serializable
{
    private static final ObjectMapper objectMapper = JacksonObjectMapperConfig.staticJsonMapper();

    private long id;

    private String name;

    private String value;

    private transient Serializable displayValue;

    protected Map<String, String> translations = new HashMap<>();

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public boolean hasValue()
    {
        return value != null;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public long getId()
    {
        return id;
    }

    public void setId( long id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    /**
     * Should be used only by Spring/Hibernate.
     */
    public void setValue( String value )
    {
        this.value = value;
    }

    /**
     * Should be used only by Spring/Hibernate.
     */
    public String getValue()
    {
        return value;
    }

    public void setDisplayValue( Serializable displayValue )
    {
        this.displayValue = displayValue;
        try
        {
            this.value = objectMapper.writeValueAsString( displayValue );
        }
        catch ( JsonProcessingException e )
        {
            log.error( String
                .format( "An error occurred while serializing system setting: '%s'", name ), e );
        }
    }

    public Serializable getDisplayValue()
    {
        if ( displayValue == null )
        {
            displayValue = convertValueToSerializable();
        }

        return displayValue;
    }

    private Serializable convertValueToSerializable()
    {
        Serializable valueAsSerializable = null;
        if ( hasValue() )
        {
            Optional<SettingKey> settingKey = SettingKey.getByName( name );

            try
            {
                if ( settingKey.isPresent() )
                {
                    Object valueAsObject = objectMapper.readValue( value, settingKey.get().getClazz() );
                    valueAsSerializable = (Serializable) valueAsObject;
                }
                else
                {
                    valueAsSerializable = StringEscapeUtils.unescapeJava( value );
                }
            }
            catch ( MismatchedInputException ex )
            {
                log.warn( "Content could not be de-serialized by Jackson", ex );
                valueAsSerializable = StringEscapeUtils.unescapeJava( value );
            }
            catch ( JsonProcessingException ex )
            {
                log.error( String
                    .format( "An error occurred while de-serializing system setting: '%s'", name ), ex );
            }
        }

        return valueAsSerializable;
    }

    public Map<String, String> getTranslations()
    {
        return translations;
    }

    public void setTranslations( Map<String, String> translations )
    {
        if ( translations != null )
        {
            this.translations = new HashMap<>( translations );
        }
        else
        {
            this.translations.clear();
        }
    }

    public Optional<String> getTranslation( String locale )
    {
        return Optional.ofNullable( translations.get( locale ) );
    }

    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }

        if ( o == null )
        {
            return false;
        }

        if ( !(o instanceof SystemSetting) )
        {
            return false;
        }

        final SystemSetting other = (SystemSetting) o;

        return name.equals( other.getName() );
    }

    @Override
    public int hashCode()
    {
        int prime = 31;
        int result = 1;

        result = result * prime + name.hashCode();

        return result;
    }

    @Override
    public String toString()
    {
        return new StringJoiner( ", ", SystemSetting.class.getSimpleName() + "[", "]" )
            .add( "id=" + id )
            .add( "name='" + name + "'" )
            .add( "value='" + value + "'" )
            .add( "displayValue=" + displayValue )
            .add( "translations=" + translations ).toString();
    }
}
