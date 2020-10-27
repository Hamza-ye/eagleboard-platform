package com.mass3d.commons.config;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import java.util.Date;
import com.mass3d.commons.config.jackson.EmptyStringToNullStdDeserializer;
import com.mass3d.commons.config.jackson.ParseDateStdDeserializer;
import com.mass3d.commons.config.jackson.WriteDateStdSerializer;
import com.mass3d.commons.config.jackson.geometry.JtsXmlModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Main Jackson Mapper configuration. Any component that requires JSON/XML
 * serialization should use the Jackson mappers configured in this class.
 * 
 */
@Configuration
public class JacksonObjectMapperConfig
{
    /*
     * Default JSON mapper
     */
    public static final ObjectMapper jsonMapper = configureMapper( new ObjectMapper() );

    /*
     * Default JSON mapper for Program Stage Instance data values
     */
    public static final ObjectMapper dataValueJsonMapper = configureMapper( new ObjectMapper(), true );

    /*
     * Default XML mapper
     */
    public static final ObjectMapper xmlMapper = configureMapper( new XmlMapper() );

    @Primary
    @Bean( "jsonMapper" )
    public ObjectMapper jsonMapper()
    {
        return jsonMapper;
    }

    @Bean( "dataValueJsonMapper" )
    public ObjectMapper dataValueJsonMapper()
    {
        return dataValueJsonMapper;
    }

    @Bean( "xmlMapper" )
    public ObjectMapper xmlMapper()
    {
        return xmlMapper;
    }

    public static ObjectMapper staticJsonMapper()
    {
        return jsonMapper;
    }

    public static ObjectMapper staticXmlMapper()
    {
        return xmlMapper;
    }

    static
    {
        JtsModule jtsModule = new JtsModule( new GeometryFactory( new PrecisionModel(), 4326 ) );
        jsonMapper.registerModule( jtsModule );
        dataValueJsonMapper.registerModule( jtsModule );
        xmlMapper.registerModule( new JtsXmlModule() );
    }

    private static ObjectMapper configureMapper( ObjectMapper objectMapper )
    {
        return configureMapper( objectMapper, false );
    }

    /**
     * Shared configuration for all Jackson mappers
     *
     * @param objectMapper an {@see ObjectMapper}
     * @param autoDetectGetters if true, enable `autoDetectGetters`
     * @return a configured {@see ObjectMapper}
     */
    private static ObjectMapper configureMapper( ObjectMapper objectMapper, boolean autoDetectGetters )
    {

        SimpleModule module = new SimpleModule();
        module.addDeserializer( String.class, new EmptyStringToNullStdDeserializer() );
        module.addDeserializer( Date.class, new ParseDateStdDeserializer() );
        module.addSerializer( Date.class, new WriteDateStdSerializer() );

        objectMapper.registerModules( module, new JavaTimeModule(), new Jdk8Module(), new Hibernate5Module() );

        objectMapper.setSerializationInclusion( JsonInclude.Include.NON_NULL );
        objectMapper.disable( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS );
        objectMapper.disable( SerializationFeature.FAIL_ON_EMPTY_BEANS );
        objectMapper.enable( SerializationFeature.WRAP_EXCEPTIONS );

        objectMapper.disable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );
        objectMapper.disable( DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY );
        objectMapper.enable( DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES );
        objectMapper.enable( DeserializationFeature.WRAP_EXCEPTIONS );

        objectMapper.disable( MapperFeature.AUTO_DETECT_FIELDS );
        objectMapper.disable( MapperFeature.AUTO_DETECT_CREATORS );
        if ( !autoDetectGetters )
        {
            objectMapper.disable( MapperFeature.AUTO_DETECT_GETTERS );
        }
        objectMapper.disable( MapperFeature.AUTO_DETECT_SETTERS );
        objectMapper.disable( MapperFeature.AUTO_DETECT_IS_GETTERS );

        return objectMapper;
    }
}
