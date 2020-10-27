package com.mass3d.webapi.controller.metadata;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.commons.util.DebugUtils;
import com.mass3d.dbms.DbmsUtils;
import com.mass3d.dxf2.metadata.MetadataImportParams;
import com.mass3d.dxf2.metadata.MetadataImportService;
import com.mass3d.security.SecurityContextRunnable;
import com.mass3d.system.notification.NotificationLevel;
import com.mass3d.system.notification.Notifier;
import com.mass3d.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope( "prototype" )
@Slf4j
public class MetadataAsyncImporter extends SecurityContextRunnable
{
    @Autowired
    private MetadataImportService metadataImportService;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private IdentifiableObjectManager manager;

    @Autowired
    private Notifier notifier;

    private MetadataImportParams params;

    @Override
    public void call()
    {
        // This is to fix LazyInitializationException
        if ( params.getUser() != null )
        {
            params.setUser( manager.get( User.class, params.getUser().getUid() ) );
        }

        if ( params.getOverrideUser() != null )
        {
            params.setOverrideUser( manager.get( User.class, params.getOverrideUser().getUid() ) );
        }

        metadataImportService.importMetadata( params );
    }

    @Override
    public void before()
    {
        DbmsUtils.bindSessionToThread( sessionFactory );
    }

    @Override
    public void after()
    {
        DbmsUtils.unbindSessionFromThread( sessionFactory );
    }

    @Override
    public void handleError( Throwable ex )
    {
        log.error( DebugUtils.getStackTrace( ex ) );
        notifier.notify( params.getId(), NotificationLevel.ERROR, "Process failed: " + ex.getMessage(), true );
    }

    public void setParams( MetadataImportParams params )
    {
        this.params = params;
    }
}
