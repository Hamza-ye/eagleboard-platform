package com.mass3d.version;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.version.VersionService" )
public class DefaultVersionService
    implements VersionService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final VersionStore versionStore;

    public DefaultVersionService( VersionStore versionStore )
    {
        checkNotNull( versionStore );

        this.versionStore = versionStore;
    }

    // -------------------------------------------------------------------------
    // VersionService implementation
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public long addVersion( Version version )
    {
        versionStore.save( version );
        return version.getId();
    }

    @Override
    @Transactional
    public void updateVersion( Version version )
    {
        versionStore.update( version );
    }

    @Override
    @Transactional
    public void updateVersion( String key )
    {
        updateVersion( key, UUID.randomUUID().toString() );
    }

    @Override
    @Transactional
    public void updateVersion( String key, String value )
    {
        Version version = getVersionByKey( key );
        
        if ( version == null )
        {
            version = new Version( key, value );
            addVersion( version );
        }
        else
        {
            version.setValue( value );
            updateVersion( version );
        }
    }

    @Override
    @Transactional
    public void deleteVersion( Version version )
    {
        versionStore.delete( version );
    }

    @Override
    @Transactional(readOnly = true)
    public Version getVersion( long id )
    {
        return versionStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public Version getVersionByKey( String key )
    {
        return versionStore.getVersionByKey( key );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Version> getAllVersions()
    {
        return versionStore.getAll();
    }
}
