package com.mass3d.keyjsonvalue;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import com.mass3d.metadata.version.MetadataVersionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.keyjsonvalue.MetaDataKeyJsonService" )
public class DefaultMetadataKeyJsonService implements MetadataKeyJsonService
{
    private final KeyJsonValueStore keyJsonValueStore;

    public DefaultMetadataKeyJsonService(
        KeyJsonValueStore keyJsonValueStore )
    {
        checkNotNull( keyJsonValueStore );

        this.keyJsonValueStore = keyJsonValueStore;
    }

    @Override
    @Transactional( readOnly = true )
    public KeyJsonValue getMetaDataVersion( String key )
    {
        return keyJsonValueStore.getKeyJsonValue( MetadataVersionService.METADATASTORE, key );
    }

    @Override
    @Transactional
    public void deleteMetaDataKeyJsonValue( KeyJsonValue keyJsonValue )
    {
        keyJsonValueStore.delete( keyJsonValue );
    }

    @Override
    @Transactional
    public long addMetaDataKeyJsonValue( KeyJsonValue keyJsonValue )
    {
        keyJsonValueStore.save( keyJsonValue );

        return keyJsonValue.getId();
    }

    @Override
    public List<String> getAllVersions()
    {
        return keyJsonValueStore.getKeysInNamespace( MetadataVersionService.METADATASTORE );
    }
}
