package com.mass3d.keyjsonvalue;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import com.mass3d.metadata.version.MetadataVersionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.keyjsonvalue.KeyJsonValueService" )
public class DefaultKeyJsonValueService
    implements KeyJsonValueService
{
    private final KeyJsonValueStore keyJsonValueStore;

    private final ObjectMapper jsonMapper;

    public DefaultKeyJsonValueService( KeyJsonValueStore keyJsonValueStore, ObjectMapper jsonMapper )
    {
        this.jsonMapper = jsonMapper;
        checkNotNull( keyJsonValueStore );

        this.keyJsonValueStore = keyJsonValueStore;
    }

    // -------------------------------------------------------------------------
    // KeyJsonValueService implementation
    // -------------------------------------------------------------------------

    @Override
    @Transactional( readOnly = true )
    public List<String> getNamespaces( boolean isAdmin )
    {
        List<String> namespaces = keyJsonValueStore.getNamespaces();
        if ( !isAdmin )
        {
            namespaces.remove( MetadataVersionService.METADATASTORE );
        }

        return namespaces;
    }

    @Override
    @Transactional( readOnly = true )
    public List<String> getKeysInNamespace( String namespace, Date lastUpdated, boolean isAdmin )
    {
        if ( !isAdmin && MetadataVersionService.METADATASTORE.equals( namespace ) )
        {
            return Collections.emptyList();
        }

        return keyJsonValueStore.getKeysInNamespace( namespace, lastUpdated );
    }

    @Override
    @Transactional( readOnly = true )
    public KeyJsonValue getKeyJsonValue( String namespace, String key, boolean isAdmin )
    {
        if ( !isAdmin && MetadataVersionService.METADATASTORE.equals( namespace ) )
        {
            return null;
        }

        return keyJsonValueStore.getKeyJsonValue( namespace, key );
    }

    @Override
    @Transactional( readOnly = true )
    public List<KeyJsonValue> getKeyJsonValuesInNamespace( String namespace, boolean isAdmin )
    {
        if ( !isAdmin && MetadataVersionService.METADATASTORE.equals( namespace ) )
        {
            return Collections.emptyList();
        }

        return keyJsonValueStore.getKeyJsonValueByNamespace( namespace );
    }

    @Override
    @Transactional
    public Long addKeyJsonValue( KeyJsonValue keyJsonValue )
    {
        if ( MetadataVersionService.METADATASTORE.equals( keyJsonValue.getNamespace() ) )
        {
            return null;
        }

        keyJsonValueStore.save( keyJsonValue );

        return keyJsonValue.getId();
    }

    @Override
    @Transactional
    public void updateKeyJsonValue( KeyJsonValue keyJsonValue )
    {
        if ( MetadataVersionService.METADATASTORE.equals( keyJsonValue.getNamespace() ) )
        {
            return;
        }

        keyJsonValueStore.update( keyJsonValue );
    }

    @Override
    @Transactional
    public void deleteNamespace( String namespace )
    {
        if ( MetadataVersionService.METADATASTORE.equals( namespace ) )
        {
            return;
        }

        keyJsonValueStore.getKeyJsonValueByNamespace( namespace ).forEach( keyJsonValueStore::delete );
    }

    @Override
    @Transactional
    public void deleteKeyJsonValue( KeyJsonValue keyJsonValue )
    {
        if ( MetadataVersionService.METADATASTORE.equals( keyJsonValue.getNamespace() ) )
        {
            return;
        }

        keyJsonValueStore.delete( keyJsonValue );
    }

    @Override
    @Transactional( readOnly = true )
    public <T> T getValue( String namespace, String key, Class<T> clazz )
    {
        KeyJsonValue keyJsonValue = keyJsonValueStore.getKeyJsonValue( namespace, key );

        if ( keyJsonValue == null || keyJsonValue.getJbPlainValue() == null )
        {
            return null;
        }

        try
        {
            return jsonMapper.readValue( keyJsonValue.getJbPlainValue(), clazz );
        }
        catch ( JsonProcessingException ex )
        {
            throw new UncheckedIOException( ex );
        }
    }

    @Override
    @Transactional
    public <T> void addValue( String namespace, String key, T object )
    {
        try
        {
            String value = jsonMapper.writeValueAsString( object );
            KeyJsonValue keyJsonValue = new KeyJsonValue( namespace, key, value, false );
            keyJsonValueStore.save( keyJsonValue );
        }
        catch ( JsonProcessingException ex )
        {
            throw new UncheckedIOException( ex );
        }
    }

    @Override
    @Transactional
    public <T> void updateValue( String namespace, String key, T object )
    {
        KeyJsonValue keyJsonValue = keyJsonValueStore.getKeyJsonValue( namespace, key );

        if ( keyJsonValue == null )
        {
            throw new IllegalStateException( String.format(
                "No object found for namespace '%s' and key '%s'", namespace, key ) );
        }

        try
        {
            String value = jsonMapper.writeValueAsString( object );
            keyJsonValue.setValue( value );
            keyJsonValueStore.update( keyJsonValue );
        }
        catch ( JsonProcessingException ex )
        {
            throw new UncheckedIOException( ex );
        }
    }
}
