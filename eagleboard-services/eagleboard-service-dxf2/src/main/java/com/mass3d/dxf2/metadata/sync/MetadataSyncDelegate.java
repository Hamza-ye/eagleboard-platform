package com.mass3d.dxf2.metadata.sync;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.mass3d.dxf2.metadata.systemsettings.DefaultMetadataSystemSettingService;
import com.mass3d.render.RenderFormat;
import com.mass3d.render.RenderService;
import com.mass3d.system.SystemInfo;
import com.mass3d.system.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Handling remote calls for metadata sync
 *
 */
@Slf4j
@Component( "com.mass3d.dxf2.metadata.sync.MetadataSyncDelegate" )
@Scope( "prototype" )
public class MetadataSyncDelegate
{
    @Autowired
    private DefaultMetadataSystemSettingService metadataSystemSettingService;

    @Autowired
    private RenderService renderService;

    @Autowired
    private SystemService systemService;

    public boolean shouldStopSync( String metadataVersionSnapshot )
    {
        SystemInfo systemInfo = systemService.getSystemInfo();
        String systemVersion = systemInfo.getVersion();

        if ( StringUtils.isEmpty( systemVersion ) || !metadataSystemSettingService.getStopMetadataSyncSetting() )
        {
            return false;
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream( metadataVersionSnapshot.getBytes( StandardCharsets.UTF_8 ) );
        String remoteVersion = "";

        try
        {
            JsonNode systemObject = renderService.getSystemObject( byteArrayInputStream, RenderFormat.JSON );

            if ( systemObject == null )
            {
                return false;
            }

            remoteVersion = systemObject.get( "version" ).textValue();

            if ( StringUtils.isEmpty( remoteVersion ) )
            {
                return false;
            }
        }
        catch ( IOException e )
        {
            log.error( "Exception occurred when parsing the metadata snapshot" + e.getMessage() );
        }

        return !systemVersion.trim().equals( remoteVersion.trim() );
    }
}
